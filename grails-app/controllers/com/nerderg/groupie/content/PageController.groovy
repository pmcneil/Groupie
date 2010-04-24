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
import com.nerderg.groupie.Requestmap
import com.nerderg.groupie.BaseController

class PageController extends BaseController {
    def authenticateService
    def permissionService
    def contentService

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def pages = Item.findAllByType('layout', params)
        [ pageList: pages, pageTotal: pages.size ]
    }

    def show = {
        def contentInstance = Content.get( params.id )

        if(!contentInstance) {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
        else {

            return [ contentInstance : contentInstance ]
        }
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
        def item = Content.get(params.id)?.item
        if(!item){
            flash.message = "Couldn't find item $params.id"
            redirect(action: list)
        }

        def contentInstance = Content.get( params.id )
        if(contentInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(contentInstance.version > version) {

                    contentInstance.errors.rejectValue("version", "content.optimistic.locking.failure", "Another user has updated this Content while you were editing.")
                    render(view:'edit',model:[contentInstance:contentInstance])
                    return
                }
            }
            def newContentInstance = new Content()
            newContentInstance.properties = params
            if(!newContentInstance.hasErrors() && newContentInstance.save()) {
                item.current = newContentInstance
                flash.message = "Content ${params.id} updated"
                redirect(action:show,id:newContentInstance.id)
            }
            else {
                contentInstance.properties = params
                render(view:'edit',model:[contentInstance:contentInstance])
            }
        }
        else {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def createPage = {
        def template = Item.findByNameAndType('Default Page Template', 'template')
        def layout = Layout.findByName('main')
        def count = Item.findAllByType('page').size +1

        def page = new Content (
            title: "page $count",
            content: template?.current.content,
        )

        def item =  new Item(name: "page $count", readPerms: 'Editors', writePerms: 'Editors')
        item.params = ['layoutId': layout?.id, 'templateId' : template?.id, 'pageOrder': count]

        page.item = item
        render(template: 'create', model: [pageInst: page])
    }

    def editPage = {
        def pageInst = Content.get( params.id )

        if(!pageInst) {
            flash.message = "Content not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            render(template: 'edit', model: ['pageInst': pageInst])
        }
    }

    def updatePage = {
        def pageInst = Content.get(params.id)
        if(!pageInst) {
            flash.message = "Content not found with id ${params.id}"
            redirect(controller: 'display', action: 'index', params:[name: pageInst.item.name])
            return
        }

        def item = pageInst.item
        item.params = ['layoutId': params.layout, 'templateId' : params.template, 'pageOrder': params.pageOrder]
        item.name = params.name
        if(params.parent != 'null'){
            pageInst.item.parent = Item.get(params.parent)
        }

        //read/write Permissions
        permissionService.updatePermissions(params.readPerms, params.name)
        pageInst.item.readPerms = params.readPerms
        pageInst.item.writePerms = params.writePerms

        contentService.save(item)

        def eng = new SimpleTemplateEngine()
        def content = eng.createTemplate(params.content).make(["pageName":params.name]).toString()
        content = contentService.filterContent(content)

        def newPage = contentService.createContent(item, authenticateService.userDomain())
        newPage.title = params.title
        newPage.content = content
        item.current = newPage

        if(contentService.save(newPage) && contentService.save(item)) {
            log.debug "*** updated page $newPage.item.name - current content is $newPage.item.current.content"
            redirect(controller: 'display', action: 'index', params:[name: item.name])
        }
        else {
            redirect(controller: 'page', action: 'problem', model: ['pageInst': newPage])
        }
    }

    def savePage = {
        def pageInst = contentService.getOrCreateContent(params.name, 'page', authenticateService.userDomain())

        if(pageInst.hasErrors()){
            redirect(controller: 'page', action: 'problem', model: [pageInst: pageInst])
            return
        }

        pageInst.item.params = ['layoutId': params.layout, 'templateId' : params.template, 'pageOrder': params.pageOrder]
        if(params.parent != 'null'){
            pageInst.item.parent = Item.get(params.parent)
        }

        //read/write Permissions
        permissionService.updatePermissions(params.readPerms, params.name)
        pageInst.item.readPerms = params.readPerms
        pageInst.item.writePerms = params.writePerms

        def eng = new SimpleTemplateEngine()
        def content = eng.createTemplate(params.content).make(["pageName":params.name]).toString()
        pageInst.title = params.title
        pageInst.content = content

        if(contentService.save(pageInst) && contentService.save(pageInst.item, true)) {
            log.debug "*** saved page $pageInst.item.name - current content is $pageInst.item.current.content"
            redirect(controller: 'display', action: 'index', params:[name: pageInst.item.name])
        }
        else {
            redirect(controller: 'page', action: 'problem', model: [pageInst: pageInst])
        }
    }

    def createTemplate = {
        def template = Item.findByNameAndType('Default Page Template', 'template')
        def count = Item.findAllByType('template').size +1
        def data = [
            name: "template $count",
            content: template?.current.content,
        ]
        render(template: 'createTemplate', model: ['data': data])
    }

    /**
     * An ajax method to save a new template as content
     */
    def saveTemplate = {
        def contentInst = contentService.getOrCreateContent(params.name, 'template', authenticateService.userDomain())
        if(contentInst.hasErrors()){
            contentInst.errors.allErrors.each { log.debug it }
            renderJson("Errors creating template")
            return
        }

        contentInst.content = params.content

        if(!contentInst.hasErrors() && contentInst.save()) {
            flash.message = "Content ${contentInst.id} created"
            renderJson("New template created $contentInst.id")
        }
        else {
            pageInst.errors.allErrors.each { println it }
            renderJson("Errors creating template")
        }
    }

    def changeTemplate = {
        def template = Item.get(params.id)
        if(template) {
            def data = template.current.content.encodeAsHTML()
            println("Template Data *** \n $data")
            render(data)
        } else {
            render("oops $params.id")
        }
    }

    def revertPage = {
        def pageInst = Content.get(params.id)
        if(!pageInst) {
            flash.message = "Content not found with id ${params.id}"
            redirect(controller: 'display', action: 'index', params:[name: pageInst.item.name])
            return
        }

        def item = pageInst.item
        def revertedContent = Content.get(params.currentId)
        item.current = revertedContent

        if(contentService.save(item)) {
            log.debug "*** reverted page $item.name - current content is $item.current.content"
            redirect(controller: 'display', action: 'index', params:[name: item.name])
        }
        else {
            redirect(controller: 'page', action: 'problem', model: ['pageInst': item])
        }
    }
}
