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

class MemberDataController extends BaseController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ memberDataInstanceList: MemberData.list( params ), memberDataInstanceTotal: MemberData.count() ]
    }

    def show = {
        def memberDataInstance = MemberData.get( params.id )

        if(!memberDataInstance) {
            flash.message = "MemberData not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ memberDataInstance : memberDataInstance ] }
    }

    def delete = {
        def memberDataInstance = MemberData.get( params.id )
        if(memberDataInstance) {
            try {
                memberDataInstance.delete(flush:true)
                flash.message = "MemberData ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "MemberData ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "MemberData not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def memberDataInstance = MemberData.get( params.id )

        if(!memberDataInstance) {
            flash.message = "MemberData not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ memberDataInstance : memberDataInstance ]
        }
    }

    def update = {
        def memberDataInstance = MemberData.get( params.id )
        if(memberDataInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(memberDataInstance.version > version) {
                    
                    memberDataInstance.errors.rejectValue("version", "memberData.optimistic.locking.failure", "Another user has updated this MemberData while you were editing.")
                    render(view:'edit',model:[memberDataInstance:memberDataInstance])
                    return
                }
            }
            memberDataInstance.properties = params
            if(!memberDataInstance.hasErrors() && memberDataInstance.save()) {
                flash.message = "MemberData ${params.id} updated"
                redirect(action:show,id:memberDataInstance.id)
            }
            else {
                render(view:'edit',model:[memberDataInstance:memberDataInstance])
            }
        }
        else {
            flash.message = "MemberData not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def ajaxUpdate = {
        def memberDataInstance = MemberData.get( params.id )
        if(memberDataInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(memberDataInstance.version > version) {

                    memberDataInstance.errors.rejectValue("version", "memberData.optimistic.locking.failure", "Another user has updated this MemberData while you were editing.")
                    renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: memberDataInstance]) )
                    return
                }
            }
            memberDataInstance.properties = params
            if(!memberDataInstance.hasErrors() && memberDataInstance.save()) {
                renderJson([ok: "<div  class='message'> Member data updated</div>"])
            } else {
                renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: memberDataInstance]) )
            }

        }
        else {
            renderJson(errors: "<div class='errors'>Member data not found with id ${params.id}</div>")
        }
    }

    def create = {
        def memberDataInstance = new MemberData()
        memberDataInstance.properties = params
        return ['memberDataInstance':memberDataInstance]
    }

    def save = {
        def memberDataInstance = new MemberData(params)
        if(!memberDataInstance.hasErrors() && memberDataInstance.save()) {
            flash.message = "Member Data ${memberDataInstance.id} created"
            redirect(action:show,id:memberDataInstance.id)
        }
        else {
            render(view:'create',model:[memberDataInstance:memberDataInstance])
        }
    }

    def ajaxSave = {
        def memberDataInstance = new MemberData(params)
        if(!memberDataInstance.hasErrors() && memberDataInstance.save()) {
            renderJson([ok: "<div  class='message'> Member data saved</div>"])
        } else {
            renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: memberDataInstance]) )
        }
    }
}
