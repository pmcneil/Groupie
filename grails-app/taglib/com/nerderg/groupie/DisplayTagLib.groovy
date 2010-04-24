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

package com.nerderg.groupie

import com.nerderg.groupie.content.*
import org.codehaus.groovy.grails.web.pages.*
import groovy.text.Template
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import org.codehaus.groovy.grails.web.servlet.mvc.*
import com.nerderg.groupie.member.*
import java.text.SimpleDateFormat

class DisplayTagLib {
    
    static namespace = "groupie"

    def GroovyPagesTemplateEngine groovyPagesTemplateEngine
    def authenticateService
    def contentService
    def permissionService
    def memberService
    def mimeService
    def memberController

    def includeContent = {attrs ->
        if(attrs.name || attrs.content) {
            def content = attrs.content
            if(!content) {
                content = contentService.getOrCreateContent(attrs.name, (attrs.type ? attrs.type : attrs.template), authenticateService.userDomain())
                if(content?.hasErrors()){
                    content.errors.allErrors.each { 
                        log.debug it
                        out << it
                    }
                    return
                }
            }
            def template = attrs.template ? "/display/$attrs.template" : "/display/$attrs.type"
            def data = [content: content, 'attrs': attrs, 'authenticateService': authenticateService]
            def dd = ['data': data]
            out << render(template: template, model: dd )
        }
    }

    /**
     *show named content 
     */
    def show = { attrs, body ->
        attrs.template = 'content'
        attrs.type = 'content'
        attrs.cssclass = 'embeded editable'
        attrs.body = body()
        includeContent.call(attrs)
    }

    def showtagged = { attrs, body ->
        attrs.template = 'content'
        attrs.type = 'content'
        attrs.cssclass = 'embeded editable'
        attrs.body = body()
        def itemList = contentService.getTaggedContent(attrs.tag, attrs.limit)
        if(itemList.size() > 0){
            if(attrs.random){
                def i = contentService.rnd.nextInt(itemList.size())
                attrs.content = itemList[i].current
                includeContent.call(attrs)
            } else {
                itemList.each {
                    attrs.content = it.current
                    includeContent.call(attrs)
                }
            }
        } else {
            out << "$attrs.tag"
        }
    }

    def favicon = {attrs ->
        def url = attrs.remove('src')
        def icon = Media.createCriteria().get(){
            like("filename",'%favicon%')
            maxResults(1)
            order("id", "desc")
        }
        if(icon) {
            out << "<link rel='shortcut icon' href='media/show/$icon.id?w=32' type='image/x-icon' />"
        } else {
            out << "<link rel='shortcut icon' href='$url' type='image/x-icon' />"
        }
    }

    def media = {attrs ->
        def media = Media.get(attrs.mid)
        def type = mimeService.duckTyper(media.mimeType)
        if(media){
            out << render(template: '/media/mediaInner', model: [mediaInstance: media, type: type])
        } else {
            out << "media id not found: $attrs.mid"
        }
    }

    def style = { attrs -> //looking for name
        attrs.template = 'style'
        includeContent.call(attrs)
    }

    def css = {attrs ->
        if(attrs.names) {
            attrs.names.each { name ->
                def link = g.createLink(controller:'display', action:'css', params: [name: name])
                out << "<link rel='stylesheet' href='$link' type='text/css'/>"
            }
        }
    }

    def script = {attrs ->
        if(attrs.names) {
            attrs.names.each { name ->
                def link = g.createLink(controller:'display', action:'script', params: [name: name])
                out << "<script type='text/javascript' src='$link'></script>"
            }
        }
    }

    def header = { attrs, body -> //looking for name
        attrs.template = 'header'
        attrs.body = body()
        attrs.cssclass = 'editable'
        includeContent.call(attrs)
    }

    def sidebar = { attrs, body ->
        attrs.template = 'sidebar'
        attrs.type = 'content'
        attrs.cssclass = 'editable'
        attrs.body = body()
        includeContent.call(attrs)
    }

    def bd = { attrs, body ->
        out << '<div id="bd">'
        attrs.template = 'main'
        attrs.type = 'content'
        attrs.cssclass = 'grid editable'
        String bodyStr = body()
        bodyStr = bodyStr.trim()
        if(!bodyStr.empty) {
            attrs.cssclass += ' block' //show sidebar
        }
        includeContent.call(attrs)
        out << bodyStr
        out << '</div>'
    }

    def footer = { attrs, body ->
        attrs.template = 'footer'
        attrs.body = body()
        attrs.cssclass = 'editable'
        includeContent.call(attrs)
    }

    def menu = {attrs ->
        attrs.template = 'content'
        attrs.type = 'menu'
        attrs.cssclass = 'embeded'
        includeContent.call(attrs)
    }

    def module = {attrs ->
        attrs.template = 'content'
        attrs.type = 'module'
        attrs.cssclass = 'embeded'
        includeContent.call(attrs)
    }

    def pages = {attrs ->
        def pages
        int depth = attrs.depth == null ? 10 : attrs.depth.toInteger()
        
        if(depth > 0){
            if(attrs.name){
                def menu = Item.findByNameAndType(attrs.name, 'menu')
                if(menu){
                    pages = menu?.children
                }
            } else {
                pages = attrs.pages
            }
            pages = pages.grep{ permissionService.isReadable(it)}
            pages = pages.sort{ Item a, Item b ->
                a.params.pageOrder <=> b.params.pageOrder
            }
        }
        if(depth > 0) {
            depth = depth - 1
        }
        log.debug "Menu $attrs.name depth = $depth"
        out << render(template: '/tags/pages', model: ['pages': pages, 'depth': depth])
    }

    def login = {attrs ->
        out << render(template: '/tags/login')
    }

    def admin = {attrs ->
        out << render(template: '/tags/admin')
    }

    def join = {attrs ->
        def memberInstance = new Member()
        def addressInstance = new Address()
        memberInstance.properties = params
        if(authenticateService.isLoggedIn()){
            memberInstance.user = authenticateService.userDomain()
        }
        out << render(template: '/member/signup', model: ['memberInstance':memberInstance, 'person': memberInstance.user, 'addressInstance': addressInstance])
    }

    def displayContent = { attrs ->
        Content content = attrs.content
        out << content.content.encodeAsHTML()
    }

    def displayRawContent = { attrs ->

        Content content = attrs.content
        out << content.content

    }


    def processContent = { attrs ->
        def model = attrs.model

        if(model != null && model.content != null) {

            def content = model.content
            def type = content.item.type
            def mimeType = content.mimeType
            log.debug "processing $content - Model is $model"
            if(type != 'module' && mimeType.startsWith('text/html')) {
                //process any GSP in the content
                log.debug "process embeded $content"
                def ec = contentService.embedContent(content.content)
                content.content = ec
                contentService.processGSPContent(model, content.title, out)
            } else if(mimeType.startsWith('text/javascript')) {
                out << content.content
            } else if(mimeType.startsWith('text')) {
                contentService.processGSPContent(model, content.title, out)
            }
            if(mimeType.startsWith('image')) {
                out << "<img src='" + g.createLinkTo(dir: 'images', file: content.content) + "' alt='$content.title' />"
            }
            log.debug "done processing $content"
        }
    }

    def codeEditor = { attrs ->
        out << render(template: '/tags/code_editor', model: [editorName: attrs.name, editorType: attrs.type, items: attrs.items, contentInst: attrs.content, action: attrs.action])
    }

    def linkedFieldEditor = { attrs ->
        out << render(template: '/tags/linked_field_editor', model: [
                label: attrs.label,
                field: attrs.field,
                inst: attrs.inst,
                instName: attrs.instName,
                controller: attrs.controller,
                reload: attrs.reload])
    }

    def userEditor = {attrs ->
        def user = attrs.user
        if(user){
            memberService.buildUserModel(user)
            out << render(template: '/user/user_details_form', model: memberService.buildUserModel(user))
        } else {
            out << "<div class='errors'>can't find null user.</div>"
        }
    }

    def dateFormat = {attrs ->
        def fmtString = attrs.format
        Date date = attrs.date
        SimpleDateFormat f = new SimpleDateFormat(fmtString)
        out << f.format(date)
    }

}