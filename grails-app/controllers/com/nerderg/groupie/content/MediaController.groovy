/*
    Copyright 2009, 2010 Peter McNeil

    This file is part of Groupie.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not 
    use this file except in compliance with the License. You may obtain a copy 
    of the License at http://www.apache.org/licenses/LICENSE-2.0 
    
    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License. 
*/

package com.nerderg.groupie.content
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import org.springframework.web.multipart.*

import org.hibernate.*;
import org.hibernate.Criteria
import org.hibernate.criterion.*
import org.hibernate.criterion.Projections

class MediaController {
    def authenticateService
    def contentService
    def imageService
    def mimeService
    def sessionFactory

    static typeLikeMatch = [
        'image' : ['%image%'],
        'audio' : ['audio%', 'application/ogg%', '%mpeg%', '%x-ms-wma%'],
        'video' : ['video%','%ogv%'],
        'document': ['application/pdf',
                     'application/vnd.oasis.opendocument.text',
                     'application/vnd.oasis.opendocument.spreadsheet',
                     'application/vnd.oasis.opendocument.presentation',
                     'application/vnd.ms-powerpoint',
                     'application/msword'],
    ]

    def index = {
        redirect(action: 'list')
    }

    def upload = {
        render(template: 'upload')
    }

    def save = {
        // see http://www.jarvana.com/jarvana/view/org/springframework/spring/1.2.9/spring-1.2.9-javadoc.jar!/org/springframework/web/multipart/MultipartFile.html
        MultipartFile mfile = request.getFile('media')
        def mimeType = mfile.getContentType()
        def type = mimeService.duckTyper(mimeType)
        log.debug "Saving media of type $mimeType"
        def media
        if(type == "image") {
            def imgType = mimeType.split('/')[1]
            byte[] image
            if(imgType == 'gif'){
                image = mfile.getBytes()
            } else {
                image = imageService.thumb(mfile.getBytes(), 1024, 768, imgType, true)
            }
            media = new Media(filename: mfile.getOriginalFilename(), data: image, mimeType: mimeType)
        } else {
            media = new Media(filename: mfile.getOriginalFilename(), data: mfile.getBytes(), mimeType: mimeType)
        }
        
        if(media){
            media.save()
            if(media.hasErrors()){
                media.errors.each { println it }
                flash.message = "Errors uploading file"
            }
            redirect(action: 'insert', params: [type: type])
        } else {
            flash.message = "Errors uploading file, no media found."
            redirect(action: 'insert')
        }
    }

    def delete = {

        def mediaInstance = Media.get( params.id )
        log.debug "Deleting media $mediaInstance.filename"
        if(mediaInstance) {
            try {
                mediaInstance.delete(flush:true)
                flash.message = "Media ${params.id} deleted"
                redirect(action:insert, params: [type: params.type])
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Media ${params.id} could not be deleted"
                redirect(action:insert, params: [type: params.type])
            }
        }
        else {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:insert, params: [type: params.type])
        }
    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ mediaInstanceList: Media.list( params ), mediaInstanceTotal: Media.count() ]
    }

    def insert = {
        log.debug "insert media params: $params"
        def typeLike = typeLikeMatch[params.type]
        log.debug "type match $typeLike"
        def max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def offset = params.offset ? params.offset.toInteger() : 0
        def mediaList = Media.createCriteria().list(){
            or {
                typeLike.each{
                    like('mimeType', it)
                }
            }
            order('id','desc')
            maxResults(max)
            firstResult(offset)
        }
        
        def mediaListCount = Media.createCriteria().get(){
            or {
                typeLike.each{
                    like('mimeType', it)
                }
            }
            projections{
                rowCount()
            }
        }
        render(template: 'insert', model: [ mediaInstanceList: mediaList, mediaInstanceTotal: mediaListCount, selectedType: params.type ])
    }

    def show = {
        log.debug("show: $params.id, w: $params.w, h: $params.h, a: $params.a ")
        def id = params.id.replaceAll(/\..../,'')
        //todo just get the dateCreated, not the data first
        def start = System.currentTimeMillis()
        def hsession = sessionFactory.getCurrentSession()
        Query q = hsession.createQuery("select dateCreated from Media where id = :id")
        q.setLong("id",id.toLong())
        Date dcreated = q.uniqueResult()
        def deltaT = System.currentTimeMillis() - start
        log.debug " create date is $dcreated (qt: $deltaT)"

        if(dcreated) {
            int ifmodsince = request.getDateHeader("If-Modified-Since")/1000
            int created = dcreated.getTime()/1000
            log.debug "mod since " + ifmodsince +" - created " + created
            if(ifmodsince > 0 && ifmodsince >= created){
                log.debug "sending 304 for $id"
                response.sendError(304)
                return
            }
        }

        start = System.currentTimeMillis()

        def media = Media.get(id.toInteger())
        deltaT = System.currentTimeMillis() - start
        log.debug "media load time $deltaT"

        if (media) {
            if(media.dateCreated == null){
                media.dateCreated = new Date()
                media.save()
            }
            response.setDateHeader("Last-Modified", media.dateCreated.getTime())
            if(media.mimeType.startsWith('image')){
                if(params.w || params.h){
                    int width = (params.w && params.w != "") ? params.w.toInteger() : null
                    int height = (params.h && params.h != "") ? params.h.toInteger() : null;
                    boolean keepAspect = params.a != null
                    def imgType = media.mimeType.split('/')[1]
                    byte[] image
                    if( width == null && height == null) {
                        image = media.data
                    } else {
                        image = imageService.thumb(media.data, width, height, imgType, keepAspect)
                    }
                    response.setContentLength(image.length)
                    writeOut(image)
                } else {
                    response.setContentLength(media.data.length)
                    writeOut(media.data)
                }
            } else {
                response.setContentType(media.mimeType)
                response.setContentLength(media.data.length)
                writeOut(media.data)
            }
        } else {
            response.sendError(404)
        }
    }

    def writeOut(data) {
        def out = response.outputStream
        try {
            out.write(data)
        } catch (e) {
            log.debug "Error writing media $e"
        } finally {
            try {
                out.flush()
                out.close()
            } catch (e){
                log.debug "problem closing output stream $e"
            }
        }
    }

    def content = {
        log.debug("content: $params.id ")
        def id = params.id.replaceAll(/media-/,'')
        def media = Media.get(id.toInteger())
        def type = mimeService.duckTyper(media.mimeType)
        if (media) {
            render(template: 'media', model: [mediaInstance: media, type: type])
        } else {
            response.sendError(404)
        }
    }
}
