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

import groovy.text.SimpleTemplateEngine
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import org.codehaus.groovy.grails.web.pages.*
import groovy.text.Template
import com.nerderg.groupie.BaseController

class ContentController extends BaseController{
    def authenticateService
    def contentService
    def GroovyPagesTemplateEngine groovyPagesTemplateEngine

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ contentInstanceList: Content.list( params ), contentInstanceTotal: Content.count() ]
    }

    def show = {
        def contentInstance = Content.get( params.id )

        if(!contentInstance) {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ contentInstance : contentInstance ] }
    }

    def delete = {
        def contentInstance = Content.get( params.id )
        if(contentInstance) {
            try {
                contentInstance.delete(flush:true)
                flash.message = "Content ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Content ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def contentInstance = Content.get( params.id )

        if(!contentInstance) {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ contentInstance : contentInstance ]
        }
    }

    def update = {
        def contentInstance = Content.get( params.id )
        def author = authenticateService.userDomain()
        log.debug "**** $author - Trying to update content $params.id *****"
        log.debug params
        if(contentInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(contentInstance.version > version) {
                    
                    contentInstance.errors.rejectValue("version", "content.optimistic.locking.failure", "Another user has updated this Content while you were editing.")
                    render(view:'edit',model:[contentInstance:contentInstance])
                    return
                }
            }
            contentInstance.properties = params
            contentInstance.author = author //overide parameter setting we want the real authenticated user always
            if(!contentInstance.hasErrors() && contentInstance.save(flush: true)) {
                flash.message = "Content ${params.id} updated"
                log.debug "Content ${params.id} updated"
                redirect(action:show,id:contentInstance.id)
            }
            else {
                render(view:'edit',model:[contentInstance:contentInstance])
            }
        }
        else {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def version = {
        def id = contentService.getItemIdFromEditableDivId(params.id)
        if(!id){
            render("Bad item id $params.id")
            return
        }

        def item = Item.get(id)
        if(!item){
            render("Couldn't find item $params.id")
            return
        }

        def newContent = contentService.filterContent(params.content)
        if(item.current) {
            if(item.current.content == newContent){
                render("No change")
                return
            }
        }

        def contentInstance = new Content()
        contentInstance.item = item
        contentInstance.author = authenticateService.userDomain()
        contentInstance.mimeType = 'text/html'
        if(item.current) {
            contentInstance.title = item.current.title
        } else {
            contentInstance.title = item.name
        }
        contentInstance.content = newContent

        if(!contentService.testContent(contentInstance.content)){
            render("Error parsing content")
            return
        }

        if(!contentInstance.hasErrors() && contentService.save(contentInstance)) {
            item.current = contentInstance
            item.save()
            render("Saved")
        } else {
            render("Error saving")
        }
    }

    
    def create = {
        def contentInstance = new Content()
        contentInstance.properties = params
        return ['contentInstance':contentInstance]
    }

    def save = {
        def contentInstance = new Content(params)
        if(!contentInstance.hasErrors() && contentInstance.save()) {
            contentInstance.item.current = contentInstance
            flash.message = "Content ${contentInstance.id} created"
            redirect(action:show,id:contentInstance.id)
        }
        else {
            render(view:'create',model:[contentInstance:contentInstance])
        }
    }

    //ajax get a content fragment by name
    def byname = {
        def name = params.name
        if(name) {
            def content = contentService.getOrCreateContent(name, 'content', authenticateService.userDomain())
            if(content.hasErrors()){
                content.errors.allErrors.each { log.debug it }
                render("Errors in creating new content")
                return
            }

            params.cssclass = 'editable'
            def data = [content: content, attrs: params]
            render(template: '/display/content', model: ['data' : data] )
        } else {
            render("bugger, no name supplied. ")
        }
    }

    def byid = {
        def id = params.id
        log.debug "id requested is $id"
        if(id) {
            def item = Item.get(id)
            if(!item){
                render("Item not found")
                return
            }

            params.cssclass = 'editable'
            def data = [content: item.current, attrs: params]
            render(template: '/display/content', model: ['data' : data] )
        } else {
            render("bugger, no id supplied. ")
        }
    }

    def revert = {
        def contentInst = Content.get(params.id)
        if(!contentInst) {
            flash.message = "Content not found with id $params.id"
            log.debug "Content not found with id $params.id"
            renderJson(g.render("Content not found with id $params.id"))
            return
        }

        def item = contentInst.item
        def revertedContent = Content.get(params.currentId)
        item.current = revertedContent

        if(contentService.save(item)) {
            log.debug "*** reverted item $item.name - current content is $item.current.content"
            //todo: replace content only instead of reload
            //render("<groupie:show name='$item.name'/>")
            redirectJson('.')
        }
        else {
            flash.message = "Errors updating content"
            //render("<groupie:show name='$item.name'/>")
            redirectJson('.')
        }
    }

    def history = {
        def id = contentService.getItemIdFromEditableDivId(params.id)
        def item = Item.get(id)
        if(!item) {
            log.debug "item id $params.id not found."
            return
        }
        render(template: "history", model: [contentInst: item.current])
    }

    def suggestContent = {
        log.debug "params $params"
        def tag = params.tagEntry
        def type = params.type.toLowerCase()
        def suggestions = contentService.getTaggedItems(tag, type, 20)
        render(template: "suggestContent", model: [suggestions: suggestions])
    }
}
