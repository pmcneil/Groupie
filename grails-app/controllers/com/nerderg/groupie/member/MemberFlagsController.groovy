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

class MemberFlagsController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ memberFlagsInstanceList: MemberFlags.list( params ), memberFlagsInstanceTotal: MemberFlags.count() ]
    }

    def show = {
        def memberFlagsInstance = MemberFlags.get( params.id )

        if(!memberFlagsInstance) {
            flash.message = "MemberFlags not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ memberFlagsInstance : memberFlagsInstance ] }
    }

    def delete = {
        def memberFlagsInstance = MemberFlags.get( params.id )
        if(memberFlagsInstance) {
            try {
                memberFlagsInstance.delete(flush:true)
                flash.message = "MemberFlags ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "MemberFlags ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "MemberFlags not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def memberFlagsInstance = MemberFlags.get( params.id )

        if(!memberFlagsInstance) {
            flash.message = "MemberFlags not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ memberFlagsInstance : memberFlagsInstance ]
        }
    }

    def update = {
        def memberFlagsInstance = MemberFlags.get( params.id )
        if(memberFlagsInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(memberFlagsInstance.version > version) {
                    
                    memberFlagsInstance.errors.rejectValue("version", "memberFlags.optimistic.locking.failure", "Another user has updated this MemberFlags while you were editing.")
                    render(view:'edit',model:[memberFlagsInstance:memberFlagsInstance])
                    return
                }
            }
            memberFlagsInstance.properties = params
            if(!memberFlagsInstance.hasErrors() && memberFlagsInstance.save()) {
                flash.message = "MemberFlags ${params.id} updated"
                redirect(action:show,id:memberFlagsInstance.id)
            }
            else {
                render(view:'edit',model:[memberFlagsInstance:memberFlagsInstance])
            }
        }
        else {
            flash.message = "MemberFlags not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def memberFlagsInstance = new MemberFlags()
        memberFlagsInstance.properties = params
        return ['memberFlagsInstance':memberFlagsInstance]
    }

    def save = {
        def memberFlagsInstance = new MemberFlags(params)
        if(!memberFlagsInstance.hasErrors() && memberFlagsInstance.save()) {
            flash.message = "MemberFlags ${memberFlagsInstance.id} created"
            redirect(action:show,id:memberFlagsInstance.id)
        }
        else {
            render(view:'create',model:[memberFlagsInstance:memberFlagsInstance])
        }
    }
}
