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

package com.nerderg.groupie.event

class RsvpController {

    def authenticateService
    def contentService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [rsvpInstanceList: Rsvp.list(params), rsvpInstanceTotal: Rsvp.count()]
    }

    def yes = {
        Event event = Event.get(params.id)
        def user = authenticateService.userDomain()
        def rsvp = Rsvp.findByEventAndUser(event, user)
        if(rsvp){
            flash.message = "Thanks $user.firstName, RSVP for event $event.name has <b>already been recorded</b>. See you there!"
            redirect(controller: "display", action: "index", params: [name: "events", rsvped: event.id])
        } else {
            rsvp = new Rsvp()
            rsvp.event = event
            rsvp.user = user
            rsvp.coming = true
            if(contentService.save(rsvp, true)){
                flash.message = "Thanks $user.firstName, RSVP for event $event.name <b>recorded</b>. See you there! "
                redirect(controller: "display", action: "index", params: [name: "events", rsvped: event.id])
            } else {
                flash.message = "RSVP for event <b>not recorded</b>. Please email webmaster"
                redirect(controller: "display", action: "index", params: [name: "events"])
            }
        }
    }

    def no = {

    }

    def create = {
        def rsvpInstance = new Rsvp()
        rsvpInstance.properties = params
        return [rsvpInstance: rsvpInstance]
    }

    def save = {
        def rsvpInstance = new Rsvp(params)
        if (rsvpInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), rsvpInstance.id])}"
            redirect(action: "show", id: rsvpInstance.id)
        }
        else {
            render(view: "create", model: [rsvpInstance: rsvpInstance])
        }
    }

    def show = {
        def rsvpInstance = Rsvp.get(params.id)
        if (!rsvpInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
            redirect(action: "list")
        }
        else {
            [rsvpInstance: rsvpInstance]
        }
    }

    def edit = {
        def rsvpInstance = Rsvp.get(params.id)
        if (!rsvpInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [rsvpInstance: rsvpInstance]
        }
    }

    def update = {
        def rsvpInstance = Rsvp.get(params.id)
        if (rsvpInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (rsvpInstance.version > version) {
                    
                    rsvpInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'rsvp.label', default: 'Rsvp')] as Object[], "Another user has updated this Rsvp while you were editing")
                    render(view: "edit", model: [rsvpInstance: rsvpInstance])
                    return
                }
            }
            rsvpInstance.properties = params
            if (!rsvpInstance.hasErrors() && rsvpInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), rsvpInstance.id])}"
                redirect(action: "show", id: rsvpInstance.id)
            }
            else {
                render(view: "edit", model: [rsvpInstance: rsvpInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def rsvpInstance = Rsvp.get(params.id)
        if (rsvpInstance) {
            try {
                rsvpInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rsvp.label', default: 'Rsvp'), params.id])}"
            redirect(action: "list")
        }
    }
}
