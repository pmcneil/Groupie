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

import com.nerderg.groupie.*
import org.springframework.security.providers.UsernamePasswordAuthenticationToken as AuthToken
import org.springframework.security.context.SecurityContextHolder as SCH

class MemberController extends BaseController{

    def authenticateService
    def daoAuthenticationProvider
    def memberService
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ memberInstanceList: Member.list( params ), memberInstanceTotal: Member.count() ]
    }

    def show = {
        def memberInstance = Member.get( params.id )

        if(!memberInstance) {
            flash.message = "Member not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ memberInstance : memberInstance ] }
    }

    def delete = {
        def memberInstance = Member.get( params.id )
        if(memberInstance) {
            try {
                memberInstance.delete(flush:true)
                flash.message = "Member ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Member ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Member not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def memberInstance = Member.get( params.id )

        if(!memberInstance) {
            flash.message = "Member not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ memberInstance : memberInstance ]
        }
    }

    def update = {
        def memberInstance = Member.get( params.id )
        if(memberInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(memberInstance.version > version) {
                    
                    memberInstance.errors.rejectValue("version", "member.optimistic.locking.failure", "Another user has updated this Member while you were editing.")
                    render(view:'edit',model:[memberInstance:memberInstance])
                    return
                }
            }
            memberInstance.properties = params
            memberInstance.user.member = memberInstance
            if(!memberInstance.hasErrors() && memberInstance.save()) {
                flash.message = "Member ${params.id} updated"
                redirect(action:show,id:memberInstance.id)
            }
            else {
                render(view:'edit',model:[memberInstance:memberInstance])
            }
        }
        else {
            flash.message = "Member not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def ajaxUpdateField = {
        log.debug "ajaxUpdateField: $params"
        def memberInstance = Member.get(params.id )
        if(memberInstance){
            if(params.date){
                memberInstance.date = Date.parse('yyyy-MM-dd', params.date)
            } else if(params.expiryDate){
                memberInstance.expiryDate = Date.parse('yyyy-MM-dd', params.expiryDate)
            } else if(params.memberType) {
                memberInstance.memberType = MemberType.get(params.memberType.toInteger())
            } else {
                memberInstance.properties = params
            }

            if(!memberInstance.hasErrors() && memberInstance.save()) {
                renderJson([ok: "<div  class='message'> Member updated</div>"])
            } else {
                renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: memberInstance]) )
            }

        }
        else {
            renderJson(errors: "<div class='errors'>Member not found with id ${params.id}</div>")
        }
    }

    def create = {
        def memberInstance = new Member()
        memberInstance.properties = params
        return ['memberInstance':memberInstance]
    }

    def suggest = {
        log.debug "member suggest $params.q"
        HashSet suggestions = new HashSet()
        suggestions.addAll(GUser.findAllByFirstNameLikeAndMemberIsNotNull("%$params.q%"))
        suggestions.addAll(GUser.findAllByLastNameLikeAndMemberIsNotNull("%$params.q%"))
        suggestions.addAll(GUser.findAllByUsernameLikeAndMemberIsNotNull("%$params.q%"))
        suggestions.addAll(GUser.findAllByEmailLikeAndMemberIsNotNull("%$params.q%"))
        log.debug "suggestions $suggestions"
        render(template: 'suggest', model: ['results': suggestions.toList()])
    }

    /* this is the new member signup flow
     *
     */

    def joinFlow = {
        start {
            action {
                def member = flow.member = memberService.getMember()

                if(flow.member){
                    return alreadyMember()
                }
                log.debug "flow is $flow"
            }
            on('alreadyMember').to('alreadyMember')
            on('success').to('intro')
            on(Exception).to 'finish'
        }

        alreadyMember {
            on("next").to("finish")
            on(Exception).to 'finish'
        }

        intro {
            on("next").to("signup")
            on("cancel").to("finish")
        }

        signup{
            action {
                if(flow.member){
                    flow.uc = new UserCommand(conversation.member.user)
                    flow.mc = new MemberCommand(conversation.member)
                    return already()
                } else if(authenticateService.isLoggedIn()){
                    flow.uc = new UserCommand(authenticateService.userDomain())
                    if(!flow.mc){
                        flow.mc = new MemberCommand()
                    }
                    log.debug "flow is $flow"
                    log.debug "goto make member"
                    return makeMember()
                } else {
                    if(!flow.uc){
                        flow.uc = new UserCommand()
                    }
                    log.debug "flow is $flow"
                    log.debug "goto register"
                    return register()
                }
            }
            on("already").to("alreadyMember")
            on("makeMember").to("membershipDetails")
            on("register").to("registerUser")
        }

        registerUser {
            on("login") {
                def auth
                try {
                    auth = new AuthToken(params.j_username, params.j_password)
                    def authtoken = daoAuthenticationProvider.authenticate(auth)
                    SCH.context.authentication = authtoken
                    flow.member = conversation.member = memberService.getMember()
                    log.debug "+++ member $conversation.member"
                } catch(Exception e) {
                    log.error("Login Fail ${params.j_username} $e.message")
                    flash.message = "Login Failed"
                    return error()
                }
            }.to("signup")

            on("next") {UserCommand uc ->
                flow.uc = uc
                if(uc.hasErrors()) {
                    return error()
                }
                if (uc.passwd != uc.repasswd) {
                    log.debug "-|- password fail **"
                    uc.errors.rejectValue('passwd', 'user.password.doesnotmatch')
                    flash.message = 'The passwords you entered did not match.'
                    return error()
                }
                if (params.captcha.toUpperCase() != session.captcha) {
                    log.debug "-|- captcha fail " + params.captcha.toUpperCase() +" != $session.captcha**"
                    flash.message = 'Code did not match.'
                    return error()
                }
            }.to("membershipDetails")

            on("back").to("intro")
            on("cancel").to("finish")
        }

        membershipDetails {
            on("next") {MemberCommand mc ->
                flow.mc = mc
                if(mc.hasErrors()) {
                    return error()
                }

            }.to("confirm")
            on("back").to("mdback")
            on("cancel").to("finish")
        }

        mdback {
            action {
                if(authenticateService.isLoggedIn()) {
                    log.debug "returning back"
                    return back()
                } else {
                    return start()
                }
            }
            on("back").to("intro")
            on("start").to("signup")
        }

        confirm {
            //show a summary
            on("next") {
                def member = memberService.makeMember(flow.uc, flow.mc)
                if(!member){
                    flash.message = "I'm sorry there was a problem with your details. Please try again."
                    return error()
                }
            }.to("thanks")
            on("back"){
                conversation.mc = flow.mc
                conversation.uc = flow.uc
            }.to("signup")
            on("cancel").to("finish")
            on(Exception).to 'finish'
        }

        thanks {
            on("next").to("finish")
            on(Exception).to 'finish'
        }

        finish {
            redirect(controller: 'display', action: 'index')
        }
    }

}
