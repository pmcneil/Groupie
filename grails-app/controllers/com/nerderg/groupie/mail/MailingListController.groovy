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

package com.nerderg.groupie.mail

class MailingListController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [mailingListInstanceList: MailingList.list(params), mailingListInstanceTotal: MailingList.count()]
    }

    def create = {
        def mailingListInstance = new MailingList()
        mailingListInstance.properties = params
        return [mailingListInstance: mailingListInstance]
    }

    def save = {
        def mailingListInstance = new MailingList(params)
        if (mailingListInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'mailingList.label', default: 'MailingList'), mailingListInstance.id])}"
            redirect(action: "show", id: mailingListInstance.id)
        }
        else {
            render(view: "create", model: [mailingListInstance: mailingListInstance])
        }
    }

    def show = {
        def mailingListInstance = MailingList.get(params.id)
        if (!mailingListInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
            redirect(action: "list")
        }
        else {
            [mailingListInstance: mailingListInstance]
        }
    }

    def edit = {
        def mailingListInstance = MailingList.get(params.id)
        if (!mailingListInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [mailingListInstance: mailingListInstance]
        }
    }

    def update = {
        def mailingListInstance = MailingList.get(params.id)
        if (mailingListInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (mailingListInstance.version > version) {
                    
                    mailingListInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'mailingList.label', default: 'MailingList')] as Object[], "Another user has updated this MailingList while you were editing")
                    render(view: "edit", model: [mailingListInstance: mailingListInstance])
                    return
                }
            }
            mailingListInstance.properties = params
            if (!mailingListInstance.hasErrors() && mailingListInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'mailingList.label', default: 'MailingList'), mailingListInstance.id])}"
                redirect(action: "show", id: mailingListInstance.id)
            }
            else {
                render(view: "edit", model: [mailingListInstance: mailingListInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def mailingListInstance = MailingList.get(params.id)
        if (mailingListInstance) {
            try {
                mailingListInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailingList.label', default: 'MailingList'), params.id])}"
            redirect(action: "list")
        }
    }
}
