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

class TagController extends BaseController {
    def contentService

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def tag = {
        def id = contentService.getItemIdFromEditableDivId(params.id)
        def item = Item.get(id)
        if(!item) {
            log.debug "item id $params.id not found."
            render(text: "Content not defined. Please select (edit) content.")
        }
        render(template: 'tag', model: ['itemInstance': item])
    }

    def suggest = {
        log.debug "tag suggest $params.q"
        def suggestions = Tag.findAllByNameLike("$params.q%")
        log.debug "suggestions $suggestions"
        render(template: 'suggest', model: ['results': suggestions])
    }

    def apply = {
        log.debug "applying tag $params.tagEntry"

        def item = Item.get(params.id)
        if(!item) {
            log.debug "item id $params.id not found."
            flash.message = "Can't find the content you wanted to tag. Please reselect the content."
            redirectJson('.')
            return
        }

        def tag
        def tagName = params.tagEntry?.trim()
        if(tagName && tagName != ""){
            tag = Tag.findByName(params.tagEntry)
            if(!tag){
                tag = new Tag(name: params.tagEntry)
                if(!contentService.save(tag)){
                    flash.message = "Problem creating new tag"
                    renderJson(g.render(template: 'tag', model: ['itemInstance': item]))
                    return
                }
            }
        } else {
            //blank or no tag entered
            renderJson([close: true])
            return
        }
        item.addToTags(tag)
        renderJson(g.render(template: 'tag', model: ['itemInstance': item]))
        return
    }

    def remove = {
        log.debug "removing tag $params.id"

        def item = Item.get(params.id)
        if(!item) {
            log.debug "item id $params.id not found."
            flash.message = "Can't find the content you wanted to untag. Please reselect the content."
            redirectJson('.')
            return
        }

        def tag = Tag.get(params.tag)
        if(!tag){
            log.debug "tag id $params.tag not found."
            flash.message = "Can't find the tag you wanted to remove."
            renderJson(g.render(template: 'tag', model: ['itemInstance': item]))
            return
        }
        
        item.removeFromTags(tag)
        renderJson(g.render(template: 'tag', model: ['itemInstance': item]))
        return
    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ tagInstanceList: Tag.list( params ), tagInstanceTotal: Tag.count() ]
    }

    def show = {
        def tagInstance = Tag.get( params.id )

        if(!tagInstance) {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ tagInstance : tagInstance ] }
    }

    def delete = {
        def tagInstance = Tag.get( params.id )
        if(tagInstance) {
            try {
                tagInstance.delete(flush:true)
                flash.message = "Tag ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Tag ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def tagInstance = Tag.get( params.id )

        if(!tagInstance) {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ tagInstance : tagInstance ]
        }
    }

    def update = {
        def tagInstance = Tag.get( params.id )
        if(tagInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(tagInstance.version > version) {
                    
                    tagInstance.errors.rejectValue("version", "tag.optimistic.locking.failure", "Another user has updated this Tag while you were editing.")
                    render(view:'edit',model:[tagInstance:tagInstance])
                    return
                }
            }
            tagInstance.properties = params
            if(!tagInstance.hasErrors() && tagInstance.save()) {
                flash.message = "Tag ${params.id} updated"
                redirect(action:show,id:tagInstance.id)
            }
            else {
                render(view:'edit',model:[tagInstance:tagInstance])
            }
        }
        else {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def tagInstance = new Tag()
        tagInstance.properties = params
        return ['tagInstance':tagInstance]
    }

    def save = {
        def tagInstance = new Tag(params)
        if(!tagInstance.hasErrors() && tagInstance.save()) {
            flash.message = "Tag ${tagInstance.id} created"
            redirect(action:show,id:tagInstance.id)
        }
        else {
            render(view:'create',model:[tagInstance:tagInstance])
        }
    }
}
