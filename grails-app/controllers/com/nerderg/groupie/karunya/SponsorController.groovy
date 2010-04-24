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

package com.nerderg.groupie.karunya

import com.nerderg.FlowService
import com.nerderg.groupie.content.Item
import com.nerderg.groupie.content.Tag
import com.nerderg.groupie.donate.*
import com.nerderg.groupie.member.*
import org.springframework.security.providers.UsernamePasswordAuthenticationToken as AuthToken
import org.springframework.security.context.SecurityContextHolder as SCH
import com.nerderg.groupie.BaseController

class TargetCommand implements Serializable {
    int id
    String name
    int sponsors

    String toString(){
        return "$name"
    }
}

class SponsorController extends BaseController {

    def sponsorService
    def flowService
    def memberService
    def daoAuthenticationProvider
    def authenticateService

    def index = {
        
        redirect(action: "child")
    }

    def childFlow = {
        start {
            action {
                flow.member = memberService.getMember()
                sponsorService.updateTargetsFromTaggedContent()
                def targets = []

                Target.list().each {
                    targets.add(new TargetCommand(name: it.name, id: it.id, sponsors: Sponsorship.countByTarget(it)))
                }
                flowService.cleanUpSession(flow)

                if(targets.isEmpty()){
                    return noTargetsSorry()
                }
                log.debug "targets are $targets"
                log.debug "flow is $flow"
                [children: targets]
            }
            on('noTargetsSorry').to('noTargetsSorry')
            on('success').to('intro')
            on(Exception).to 'finish'
        }

        noTargetsSorry {
            on("cancel").to("finish")
            on(Exception).to 'finish'
        }

        intro {
            on("next").to("selectChild")
            on("cancel").to("finish")
        }

        selectChild {
            on("next") {
                log.debug "params $params targets are $flow.children"
                if(params.id){
                    def tc = flow.children.find { c ->
                        c.id == params.id.toInteger()
                    }
                    log.debug "chosenone is $tc"
                    flow.chosenone = tc
                } else {
                    flash.message = "Please select a child by clicking a \"Sponsor Me\" "
                    back()
                }
            }.to("signup")

            on("pick") {
                def tc = flow.children.find { c ->
                    c.sponsors == 0
                }
                if(!tc){
                    flash.message = "All children have sponsors. If you still would like to  sponsor a child please select a child by clicking a \"Sponsor Me\" "
                    back()
                } else {
                    log.debug "chosenone is $tc"
                    flow.chosenone = tc
                }
            }.to("signup")

            on("cancel").to("finish")
            on("back").to("selectChild")
            on(Exception).to 'finish'
        }

        signup{
            action {
                if(flow.member){
                    def member = Member.get(flow.member.id)
                    if(!member.isAttached()){
                        member.attach()
                    }
                    flow.uc = new UserCommand(member.user)
                    flow.mc = new MemberCommand(member)
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
            on("already").to("confirm")
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
                    flow.member = memberService.getMember()
                    log.debug "+++ member $flow.member"
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
                if (session.captcha && (params.captcha.toUpperCase() != session.captcha)) {
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
            on("back").to("selectChild")
            on("start").to("signup")
        }

        confirm {
            //show a summary
            on("next") {
                //save everything and create unique payment id
                
                def member = flow.member ? flow.member : memberService.makeMember(flow.uc, flow.mc)
                if(!member){
                    flash.message = "I'm sorry there was a problem with your details. Please try again."
                    throw new Exception("Problem creating member from details.")
                }
                def sponsorship = sponsorService.sponsor(member, flow.chosenone, 45.00)
                if(!sponsorship){
                    flash.message = "I'm sorry there was a problem with your sponsorship. Please try again."
                    throw new Exception("Problem creating sponsorship from details.")
                }
                [sponsorship: sponsorship]
            }.to("paymentDetails")
            on("back").to("signup")
            on("cancel").to("finish")
            on(Exception).to 'finish'
        }

        paymentDetails {
            //show payment deets
            on("next").to("finish")
            on(Exception).to 'finish'
        }

        finish {
            redirect(controller: 'display', action: 'index')
        }
    }

}
