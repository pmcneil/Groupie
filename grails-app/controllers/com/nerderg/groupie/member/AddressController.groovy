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

class AddressController extends BaseController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ addressInstanceList: Address.list( params ), addressInstanceTotal: Address.count() ]
    }

    def show = {
        def addressInstance = Address.get( params.id )

        if(!addressInstance) {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ addressInstance : addressInstance ] }
    }

    def delete = {
        def addressInstance = Address.get( params.id )
        if(addressInstance) {
            try {
                addressInstance.delete(flush:true)
                flash.message = "Address ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Address ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def addressInstance = Address.get( params.id )

        if(!addressInstance) {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ addressInstance : addressInstance ]
        }
    }

    def update = {
        def addressInstance = Address.get( params.id )
        if(addressInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(addressInstance.version > version) {
                    
                    addressInstance.errors.rejectValue("version", "address.optimistic.locking.failure", "Another user has updated this Address while you were editing.")
                    render(view:'edit',model:[addressInstance:addressInstance])
                    return
                }
            }
            addressInstance.properties = params
            if(!addressInstance.hasErrors() && addressInstance.save()) {
                flash.message = "Address ${params.id} updated"
                redirect(action:show,id:addressInstance.id)
            }
            else {
                render(view:'edit',model:[addressInstance:addressInstance])
            }
        }
        else {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def ajaxUpdate = {
        def addressInstance = Address.get( params.id )
        if(addressInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(addressInstance.version > version) {

                    addressInstance.errors.rejectValue("version", "address.optimistic.locking.failure", "Another user has updated this Address while you were editing.")
                    renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: addressInstance]) )
                    return
                }
            }
            addressInstance.properties = params
            if(!addressInstance.hasErrors() && addressInstance.save()) {
                renderJson([ok: "<div  class='message'> Address updated</div>"])
            } else {
                renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: addressInstance]) )
            }

        }
        else {
            renderJson(errors: "<div class='errors'>Address not found with id ${params.id}</div>")
        }
    }

    def create = {
        def addressInstance = new Address()
        addressInstance.properties = params
        return ['addressInstance':addressInstance]
    }

    def save = {
        def addressInstance = new Address(params)
        if(!addressInstance.hasErrors() && addressInstance.save()) {
            flash.message = "Address ${addressInstance.id} created"
            redirect(action:show,id:addressInstance.id)
        }
        else {
            render(view:'create',model:[addressInstance:addressInstance])
        }
    }

    def ajaxSave = {
        def addressInstance = new Address(params)
        if(!addressInstance.hasErrors() && addressInstance.save()) {
            renderJson([ok: "<div  class='message'> Address saved</div>"])
        } else {
            renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: addressInstance]) )
        }
    }
}
