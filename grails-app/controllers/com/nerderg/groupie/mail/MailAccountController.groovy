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

class MailAccountController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [mailAccountInstanceList: MailAccount.list(params), mailAccountInstanceTotal: MailAccount.count()]
    }

    def create = {
        def mailAccountInstance = new MailAccount()
        mailAccountInstance.properties = params
        return [mailAccountInstance: mailAccountInstance]
    }

    def save = {
        def mailAccountInstance = new MailAccount(params)
        if (mailAccountInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), mailAccountInstance.id])}"
            redirect(action: "show", id: mailAccountInstance.id)
        }
        else {
            render(view: "create", model: [mailAccountInstance: mailAccountInstance])
        }
    }

    def show = {
        def mailAccountInstance = MailAccount.get(params.id)
        if (!mailAccountInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
            redirect(action: "list")
        }
        else {
            [mailAccountInstance: mailAccountInstance]
        }
    }

    def edit = {
        def mailAccountInstance = MailAccount.get(params.id)
        if (!mailAccountInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [mailAccountInstance: mailAccountInstance]
        }
    }

    def update = {
        def mailAccountInstance = MailAccount.get(params.id)
        if (mailAccountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (mailAccountInstance.version > version) {
                    
                    mailAccountInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'mailAccount.label', default: 'MailAccount')] as Object[], "Another user has updated this MailAccount while you were editing")
                    render(view: "edit", model: [mailAccountInstance: mailAccountInstance])
                    return
                }
            }
            mailAccountInstance.properties = params
            if (!mailAccountInstance.hasErrors() && mailAccountInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), mailAccountInstance.id])}"
                redirect(action: "show", id: mailAccountInstance.id)
            }
            else {
                render(view: "edit", model: [mailAccountInstance: mailAccountInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def mailAccountInstance = MailAccount.get(params.id)
        if (mailAccountInstance) {
            try {
                mailAccountInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'mailAccount.label', default: 'MailAccount'), params.id])}"
            redirect(action: "list")
        }
    }
}
