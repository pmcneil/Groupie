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
import com.nerderg.groupie.BaseController

class StyleController extends BaseCodeEditController {
    def type = 'style'
    def editor = 'css'

    def editPageStyle = {
        def styleInst
        if(params.name){
            styleInst = contentService.getOrCreateContent(params.name, "$type", authenticateService.userDomain())
            if(styleInst.hasErrors()){
                styleInst.errors.allErrors.each { log.debug it }
                flash.message = "Couldn't create or find page style - Select a style"
                redirect(action:list)
                return
            }
            log.debug "Redirect to style edit"
            render(template: 'edit', model: [contentInst: styleInst])
        } else {
            flash.message = "No page style name given - Select a style"
            redirect(action:list)
        }

    }

    def suggest = {
        log.debug "class suggest $params.q"
        def classes = params.q.split(' ');
        Content css = Item.findByNameAndType('default', 'style')?.current
        log.debug "css content is $css.content"
        def suggestions = new HashSet()
        classes.each {
            (css.content =~ /\.([^\s^\.]*/ + it + /[^\s^\.]*)/).each{all, m ->
                log.debug "css match $m"
                suggestions.add(m)
            }
        }
        log.debug "suggestions $suggestions"
        render(template: 'suggest', model: ['results': suggestions.asList()])
    }

    def insertStyle = {
        log.debug "class insertStyle: $params.contentcss"
        Item item = Item.findByNameAndType('default', 'style')
        if(!item){
            flash.message = "No default style found"
            return;
        }

        def newInst = contentService.createContent(item, authenticateService.userDomain(), item.current)
        newInst.content = newInst.content + "\n" +params.contentcss

        if(!newInst.hasErrors() && newInst.save()) {
            item.current = newInst
            item.save()
            flash.message = "Style ${item.name} updated"
            renderJson([success: true])
        }
        else {
            newInst.errors.allErrors.each { println it }
            flash.message = "Errors updating Style"
            renderJson([success: false])
        }

    }

}
