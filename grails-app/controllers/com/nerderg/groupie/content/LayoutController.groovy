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

import com.nerderg.groupie.BaseController

class LayoutController extends BaseController{
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ layoutInstanceList: Layout.list( params ), layoutInstanceTotal: Layout.count() ]
    }

    def show = {
        def layoutInstance = Layout.get( params.id )

        if(!layoutInstance) {
            flash.message = "Layout not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ layoutInstance : layoutInstance ] }
    }

    def delete = {
        def layoutInstance = Layout.get( params.id )
        if(layoutInstance) {
            try {
                layoutInstance.delete(flush:true)
                flash.message = "Layout ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Layout ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Layout not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def layoutInstance = Layout.get( params.id )

        if(!layoutInstance) {
            flash.message = "Layout not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ layoutInstance : layoutInstance ]
        }
    }

    def update = {
        def layoutInstance = Layout.get( params.id )
        if(layoutInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(layoutInstance.version > version) {
                    
                    layoutInstance.errors.rejectValue("version", "layout.optimistic.locking.failure", "Another user has updated this Layout while you were editing.")
                    render(view:'edit',model:[layoutInstance:layoutInstance])
                    return
                }
            }
            layoutInstance.properties = params
            if(!layoutInstance.hasErrors() && layoutInstance.save()) {
                flash.message = "Layout ${params.id} updated"
                redirect(action:show,id:layoutInstance.id)
            }
            else {
                render(view:'edit',model:[layoutInstance:layoutInstance])
            }
        }
        else {
            flash.message = "Layout not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def createLayout = {
        def layoutInstance = new Layout()
        layoutInstance.properties = params
        render(template: 'create', model: ['layoutInstance':layoutInstance])
    }

    def saveLayout = {
        def layoutInstance = new Layout(params)
        if(!layoutInstance.hasErrors() && layoutInstance.save()) {
            flash.message = "Layout ${layoutInstance.id} created"
            renderJson("Layout ${layoutInstance.id} created")
        }
        else {
            renderJson("Error saving layout")
        }
    }

    def save = {
        def layoutInstance = new Layout(params)
        if(!layoutInstance.hasErrors() && layoutInstance.save()) {
            flash.message = "Layout ${layoutInstance.id} created"
            redirect(action:show,id:layoutInstance.id)
        }
        else {
            render(view:'create',model:[layoutInstance:layoutInstance])
        }
    }

    def gridcss = {
        def layoutId = params.layout
        def layout = Layout.get(layoutId ? layoutId : 1)
        if(!layout){
            layout = new Layout(name: 'main', fixed: false, margin: 100, width: 800, sidebar: 'right', sidebarWidth: 320)
            layout.save()
        }
        render(view: 'gridlayout', model: [style: layout], contentType: 'text/css')
    }

}
