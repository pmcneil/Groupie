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

class MemberTypeController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ memberTypeInstanceList: MemberType.list( params ), memberTypeInstanceTotal: MemberType.count() ]
    }

    def show = {
        def memberTypeInstance = MemberType.get( params.id )

        if(!memberTypeInstance) {
            flash.message = "MemberType not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ memberTypeInstance : memberTypeInstance ] }
    }

    def delete = {
        def memberTypeInstance = MemberType.get( params.id )
        if(memberTypeInstance) {
            try {
                memberTypeInstance.delete(flush:true)
                flash.message = "MemberType ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "MemberType ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "MemberType not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def memberTypeInstance = MemberType.get( params.id )

        if(!memberTypeInstance) {
            flash.message = "MemberType not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ memberTypeInstance : memberTypeInstance ]
        }
    }

    def update = {
        def memberTypeInstance = MemberType.get( params.id )
        if(memberTypeInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(memberTypeInstance.version > version) {
                    
                    memberTypeInstance.errors.rejectValue("version", "memberType.optimistic.locking.failure", "Another user has updated this MemberType while you were editing.")
                    render(view:'edit',model:[memberTypeInstance:memberTypeInstance])
                    return
                }
            }
            memberTypeInstance.properties = params
            if(!memberTypeInstance.hasErrors() && memberTypeInstance.save()) {
                flash.message = "MemberType ${params.id} updated"
                redirect(action:show,id:memberTypeInstance.id)
            }
            else {
                render(view:'edit',model:[memberTypeInstance:memberTypeInstance])
            }
        }
        else {
            flash.message = "MemberType not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def memberTypeInstance = new MemberType()
        memberTypeInstance.properties = params
        return ['memberTypeInstance':memberTypeInstance]
    }

    def save = {
        def memberTypeInstance = new MemberType(params)
        if(!memberTypeInstance.hasErrors() && memberTypeInstance.save()) {
            flash.message = "MemberType ${memberTypeInstance.id} created"
            redirect(action:show,id:memberTypeInstance.id)
        }
        else {
            render(view:'create',model:[memberTypeInstance:memberTypeInstance])
        }
    }
}
