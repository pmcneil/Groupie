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

package com.nerderg.groupie.member
import com.nerderg.groupie.BaseController

class PhoneController extends BaseController {

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ phoneInstanceList: Phone.list( params ), phoneInstanceTotal: Phone.count() ]
    }

    def show = {
        def phoneInstance = Phone.get( params.id )

        if(!phoneInstance) {
            flash.message = "Phone not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ phoneInstance : phoneInstance ] }
    }

    def delete = {
        def phoneInstance = Phone.get( params.id )
        if(phoneInstance) {
            try {
                phoneInstance.delete(flush:true)
                flash.message = "Phone ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Phone ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Phone not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def phoneInstance = Phone.get( params.id )

        if(!phoneInstance) {
            flash.message = "Phone not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ phoneInstance : phoneInstance ]
        }
    }

    def update = {
        def phoneInstance = Phone.get( params.id )
        if(phoneInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(phoneInstance.version > version) {

                    phoneInstance.errors.rejectValue("version", "phone.optimistic.locking.failure", "Another user has updated this Phone while you were editing.")
                    render(view:'edit',model:[phoneInstance:phoneInstance])
                    return
                }
            }
            phoneInstance.properties = params
            if(!phoneInstance.hasErrors() && phoneInstance.save()) {
                flash.message = "Phone ${params.id} updated"
                redirect(action:show,id:phoneInstance.id)
            }
            else {
                render(view:'edit',model:[phoneInstance:phoneInstance])
            }
        }
        else {
            flash.message = "Phone not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def ajaxUpdate = {
        def phoneInstance = Phone.get( params.id )
        if(phoneInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(phoneInstance.version > version) {

                    phoneInstance.errors.rejectValue("version", "phone.optimistic.locking.failure", "Another user has updated this Phone while you were editing.")
                    renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: phoneInstance]) )
                    return
                }
            }
            phoneInstance.properties = params
            if(!phoneInstance.hasErrors() && phoneInstance.save()) {
                renderJson([ok: "<div  class='message'> Member data updated</div>"])
            } else {
                renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: phoneInstance]) )
            }

        }
        else {
            renderJson(errors: "<div class='errors'>Member data not found with id ${params.id}</div>")
        }
    }

    def create = {
        def phoneInstance = new Phone()
        phoneInstance.properties = params
        return ['phoneInstance':phoneInstance]
    }

    def save = {
        def phoneInstance = new Phone(params)
        if(!phoneInstance.hasErrors() && phoneInstance.save()) {
            flash.message = "Member Data ${phoneInstance.id} created"
            redirect(action:show,id:phoneInstance.id)
        }
        else {
            render(view:'create',model:[phoneInstance:phoneInstance])
        }
    }

    def ajaxSave = {
        def phoneInstance = new Phone(params)
        if(!phoneInstance.hasErrors() && phoneInstance.save()) {
            renderJson([ok: "<div  class='message'> Member data saved</div>"])
        } else {
            renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: phoneInstance]) )
        }
    }
}
