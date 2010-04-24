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

class TargetController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ targetInstanceList: Target.list( params ), targetInstanceTotal: Target.count() ]
    }

    def show = {
        def targetInstance = Target.get( params.id )

        if(!targetInstance) {
            flash.message = "Target not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ targetInstance : targetInstance ] }
    }

    def delete = {
        def targetInstance = Target.get( params.id )
        if(targetInstance) {
            try {
                targetInstance.delete(flush:true)
                flash.message = "Target ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Target ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Target not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def targetInstance = Target.get( params.id )

        if(!targetInstance) {
            flash.message = "Target not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ targetInstance : targetInstance ]
        }
    }

    def update = {
        def targetInstance = Target.get( params.id )
        if(targetInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(targetInstance.version > version) {
                    
                    targetInstance.errors.rejectValue("version", "target.optimistic.locking.failure", "Another user has updated this Target while you were editing.")
                    render(view:'edit',model:[targetInstance:targetInstance])
                    return
                }
            }
            targetInstance.properties = params
            if(!targetInstance.hasErrors() && targetInstance.save()) {
                flash.message = "Target ${params.id} updated"
                redirect(action:show,id:targetInstance.id)
            }
            else {
                render(view:'edit',model:[targetInstance:targetInstance])
            }
        }
        else {
            flash.message = "Target not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def targetInstance = new Target()
        targetInstance.properties = params
        return ['targetInstance':targetInstance]
    }

    def save = {
        def targetInstance = new Target(params)
        if(!targetInstance.hasErrors() && targetInstance.save()) {
            flash.message = "Target ${targetInstance.id} created"
            redirect(action:show,id:targetInstance.id)
        }
        else {
            render(view:'create',model:[targetInstance:targetInstance])
        }
    }
}
