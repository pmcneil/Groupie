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

class DataTagController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ dataTagInstanceList: DataTag.list( params ), dataTagInstanceTotal: DataTag.count() ]
    }

    def show = {
        def dataTagInstance = DataTag.get( params.id )

        if(!dataTagInstance) {
            flash.message = "DataTag not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ dataTagInstance : dataTagInstance ] }
    }

    def delete = {
        def dataTagInstance = DataTag.get( params.id )
        if(dataTagInstance) {
            try {
                dataTagInstance.delete(flush:true)
                flash.message = "DataTag ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "DataTag ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "DataTag not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def dataTagInstance = DataTag.get( params.id )

        if(!dataTagInstance) {
            flash.message = "DataTag not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ dataTagInstance : dataTagInstance ]
        }
    }

    def update = {
        def dataTagInstance = DataTag.get( params.id )
        if(dataTagInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(dataTagInstance.version > version) {
                    
                    dataTagInstance.errors.rejectValue("version", "dataTag.optimistic.locking.failure", "Another user has updated this DataTag while you were editing.")
                    render(view:'edit',model:[dataTagInstance:dataTagInstance])
                    return
                }
            }
            dataTagInstance.properties = params
            if(!dataTagInstance.hasErrors() && dataTagInstance.save()) {
                flash.message = "DataTag ${params.id} updated"
                redirect(action:show,id:dataTagInstance.id)
            }
            else {
                render(view:'edit',model:[dataTagInstance:dataTagInstance])
            }
        }
        else {
            flash.message = "DataTag not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def dataTagInstance = new DataTag()
        dataTagInstance.properties = params
        return ['dataTagInstance':dataTagInstance]
    }

    def save = {
        def dataTagInstance = new DataTag(params)
        if(!dataTagInstance.hasErrors() && dataTagInstance.save()) {
            flash.message = "DataTag ${dataTagInstance.id} created"
            redirect(action:show,id:dataTagInstance.id)
        }
        else {
            render(view:'create',model:[dataTagInstance:dataTagInstance])
        }
    }
}
