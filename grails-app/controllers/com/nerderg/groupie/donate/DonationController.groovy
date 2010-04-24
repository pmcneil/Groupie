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



package com.nerderg.groupie.donate

class DonationController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ donationInstanceList: Donation.list( params ), donationInstanceTotal: Donation.count() ]
    }

    def show = {
        def donationInstance = Donation.get( params.id )

        if(!donationInstance) {
            flash.message = "Donation not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ donationInstance : donationInstance ] }
    }

    def delete = {
        def donationInstance = Donation.get( params.id )
        if(donationInstance) {
            try {
                donationInstance.delete(flush:true)
                flash.message = "Donation ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Donation ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Donation not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def donationInstance = Donation.get( params.id )

        if(!donationInstance) {
            flash.message = "Donation not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ donationInstance : donationInstance ]
        }
    }

    def update = {
        def donationInstance = Donation.get( params.id )
        if(donationInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(donationInstance.version > version) {
                    
                    donationInstance.errors.rejectValue("version", "donation.optimistic.locking.failure", "Another user has updated this Donation while you were editing.")
                    render(view:'edit',model:[donationInstance:donationInstance])
                    return
                }
            }
            donationInstance.properties = params
            if(!donationInstance.hasErrors() && donationInstance.save()) {
                flash.message = "Donation ${params.id} updated"
                redirect(action:show,id:donationInstance.id)
            }
            else {
                render(view:'edit',model:[donationInstance:donationInstance])
            }
        }
        else {
            flash.message = "Donation not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def donationInstance = new Donation()
        donationInstance.properties = params
        return ['donationInstance':donationInstance]
    }

    def save = {
        def donationInstance = new Donation(params)
        if(!donationInstance.hasErrors() && donationInstance.save()) {
            flash.message = "Donation ${donationInstance.id} created"
            redirect(action:show,id:donationInstance.id)
        }
        else {
            render(view:'create',model:[donationInstance:donationInstance])
        }
    }
}
