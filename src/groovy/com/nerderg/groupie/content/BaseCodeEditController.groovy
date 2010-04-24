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

/**
 *
 * @author pmcneil
 */
class BaseCodeEditController extends BaseController {
    def authenticateService
    def contentService

    def type = "content"
    def editor = "html"

    def index = { }

    def list = {
        render(template: 'list')
    }

    def create = {
        log.debug "*** creating $type ***"
        def template = Item.findByNameAndType("default", "$type")
        def item = new Item(template)
        def count = Item.countByType("$type")
        item.name = "$type $count"
        item.type = type
        def newInst = new Content(title: "$type $count", content: contentService.makeDefaultContent(item), item: item)
        renderJson(g.render(template: 'create', model: [contentInst: newInst]), editor)
    }

    def save = {
        def newInst = contentService.getOrCreateContent(params.name, "$type", authenticateService.userDomain())
        if(newInst.hasErrors()) {
            newInst.errors.allErrors.each { log.debug it }
            flash.message = "Errors creating new $type"
            redirect(action: 'create')
            return
        }

        newInst.title = params.title
        newInst.content = params.content

        if(!newInst.hasErrors() && newInst.save()) {
            flash.message = "$type ${newInst.item.name} created"
            redirectJson('.')
        }
        else {
            newInst.errors.allErrors.each { println it }
            flash.message = "Errors creating new $type"
            renderJson(g.render(template: 'create', model: [contentInst: newInst]), editor)
        }
    }

    def edit = {
        def newInst = params.id == 'null' ? null : Item.get( params.id )?.current

        if(!newInst || newInst.item.type != "$type") {
            flash.message = "Select a $type"
            renderJson(g.render(template: 'list'), null)
        }
        else {
            renderJson(g.render(template: 'edit', model: [contentInst: newInst]), editor)
        }

    }

    def update = {
        def contentInst = Content.get(params.id)
        if(!contentInst || contentInst.item.type != "$type") {
            flash.message = "Content not found with id ${params.id}"
            redirect(action: 'list')
            return
        }
        def item = contentInst.item
        item.name = params.name
        item.save()

        def newInst = contentService.createContent(item, authenticateService.userDomain())
        newInst.title = params.title
        newInst.content = params.content

        if(!newInst.hasErrors() && newInst.save()) {
            item.current = newInst
            item.save()
            flash.message = "$type ${item.name} updated"
            redirectJson('.')
        }
        else {
            newInst.errors.allErrors.each { println it }
            flash.message = "Errors updating $type"
            renderJson(g.render(template: 'edit', model: [contentInst: newInst]), editor)
        }

    }

    def revert = {
        def contentInst = Content.get(params.id)
        if(!contentInst) {
            flash.message = "$type not found with id ${params.id}"
            renderJson(g.render("$type not found with id ${params.id}"), null)
            return
        }

        def item = contentInst.item
        def revertedContent = Content.get(params.currentId)
        item.current = revertedContent

        if(contentService.save(item)) {
            log.debug "*** reverted item $item.name - current content is $item.current.content"
            redirectJson('.')
        }
        else {
            flash.message = "Errors updating $type"
            renderJson(g.render(template: 'edit', model: [contentInst: revertedContent]), editor)
        }
    }

	
}

