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

class DisplayController {
    def permissionService
    def contentService

    def index = {

        Content.withTransaction { status ->
            status.setRollbackOnly()
        }
        def pageName = params.name ?: 'home'
        def cssnames = ["default", pageName]
        def scriptnames = ["default", pageName]
        def item = Item.findByNameAndType(pageName, 'page')
        if(!item) {
            response.sendError(404)
            return
        }
        if(permissionService.isReadable(item)) {
            def page = [content: item.current,
                cssnames: cssnames,
                scriptnames: scriptnames,
                layout: item.params.layoutId,
                writable: permissionService.isWritable(item),
                params: params
            ]
            render(view: 'page', model: [model: page])
        } else {
            redirect(controller: 'login', action: 'denied')
        }

    }

    boolean checkIfMod(content) {
        if(content){
            int ifmodsince = request.getDateHeader("If-Modified-Since")/1000
            int updated = content.lastUpdated.getTime()/1000
            log.debug "mod since " + ifmodsince +" - updated " + updated
            if(ifmodsince > 0 && ifmodsince >= updated){
                log.debug "sending 304 for content $content"
                response.sendError(304)
                return false
            }
            response.setDateHeader("Last-Modified", content.lastUpdated.getTime())
            return true
        }
        log.debug "sending 304 for empty content"
        response.sendError(304)
        return false
    }

    def css = {
        Content.withTransaction { status ->
            status.setRollbackOnly()
        }

        Content css = Item.findByNameAndType(params.name, 'style')?.current
        if (checkIfMod(css)) {
            render(view: 'css', model: [model: [content: css]], contentType: 'text/css')
        }
    }

    def script = {
        Content.withTransaction { status ->
            status.setRollbackOnly()
        }

        Content script = Item.findByNameAndType(params.name, 'script')?.current
        if (checkIfMod(script)) {
            render(view: 'script', model: [model: [content: script]], contentType: 'text/javascript')
        }
    }

    def toolbar = {
        render(template: 'editmenu', model: ['css':'toolbar'])
    }

    def tagged = {
        def tagPage = contentService.getTaggedItems(params.tag, 'page', 1)[0]
        if(tagPage) {
            redirect action: 'index', params: ['name': tagPage.name]
        } else {
            error('404')
        }
    }

}

