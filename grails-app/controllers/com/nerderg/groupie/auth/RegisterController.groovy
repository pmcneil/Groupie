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

package com.nerderg.groupie.auth

import com.nerderg.groupie.GUser
import com.nerderg.groupie.Role
import com.nerderg.groupie.BaseController

import org.springframework.security.providers.UsernamePasswordAuthenticationToken as AuthToken
import org.springframework.security.context.SecurityContextHolder as SCH

/**
 * Registration controller.
 */
class RegisterController extends BaseController{

    def authenticateService
    def daoAuthenticationProvider
    def emailerService

    static Map allowedMethods = [save: 'POST', update: 'POST']

    /**
     * User Registration Top page.
     */
    def index = {

        // skip if already logged in
        if (authenticateService.isLoggedIn()) {
            redirect action: show
            return
        }

        if (session.id) {
            def person = new GUser()
            person.properties = params
            return [person: person]
        }

        redirect uri: '/'
    }

    def reg = {

        // skip if already logged in
        if (authenticateService.isLoggedIn()) {
            redirect action: update
            return
        }

        if (session.id) {
            def person = new GUser()
            person.properties = params
            render(template: 'index', model: [person: person])
            return
        }

        redirect uri: '/'
    }

    /**
     * User Information page for current user.
     */
    def show = {

        // get user id from session's domain class.
        def user = authenticateService.userDomain()
        if (user) {
            render view: 'show', model: [person: GUser.get(user.id)]
        }
        else {
            redirect action: index
        }
    }

    /**
     * Edit page for current user.
     */
    def edit = {

        def person
        def user = authenticateService.userDomain()
        if (user) {
            person = GUser.get(user.id)
        }

        if (!person) {
            flash.message = "[Illegal Access] User not found with id ${params.id}"
            redirect action: index
            return
        }
        log.debug "edit details of $person"
        render (template: 'edit', model: [person: person])
    }

    /**
     * update action for current user's edit page
     */
    def update = {

        def person
        def user = authenticateService.userDomain()
        if (user) {
            person = GUser.get(user.id)
        }
        else {
            redirect action: index
            return
        }

        if (!person) {
            flash.message = "[Illegal Access] User not found with id ${params.id}"
            redirect action: index, id: params.id
            return
        }

        // if user want to change password. leave passwd field blank, passwd will not change.
        if (params.passwd && params.passwd.length() > 0
            && params.repasswd && params.repasswd.length() > 0) {
            if (params.passwd == params.repasswd) {
                person.passwd = authenticateService.encodePassword(params.passwd)
            }
            else {
                person.passwd = ''
                flash.message = 'The passwords you entered do not match.'
                render template: 'edit', model: [person: person]
                return
            }
        }

        person.firstName = params.firstName
        person.lastName = params.lastName
        person.email = params.email
        if (params.emailShow) {
            person.emailShow = true
        }
        else {
            person.emailShow = false
        }

        if (person.save()) {
            redirect uri: '/'
        }
        else {
            render template: 'edit', model: [person: person]
        }
    }

    /**
     * Person save action.
     */
    def save = {

        // skip if already logged in
        if (authenticateService.isLoggedIn()) {
            redirect action: show
            return
        }

        def redirectTo = params.redirectTo
        def person = new GUser()
        person.properties = params

        def config = authenticateService.securityConfig
        def defaultRole = config.security.defaultRole

        def role = Role.findByAuthority(defaultRole)
        if (!role) {
            person.passwd = ''
            flash.message = 'Default Role not found.'
            render template: 'index', model: [person: person]
            return
        }

        if (params.captcha.toUpperCase() != session.captcha) {
            log.debug "** captcha fail " + params.captcha.toUpperCase() +" != $session.captcha**"
            person.passwd = ''
            flash.message = 'Access code did not match.'
            render template: 'index', model: [person: person]
            return
        }

        if (params.passwd != params.repasswd) {
            person.passwd = ''
            flash.message = 'The passwords you entered do not match.'
            render template: 'index', model: [person: person]
            return
        }

        def pass = authenticateService.encodePassword(params.passwd)
        person.passwd = pass
        person.enabled = true
        person.emailShow = true
        person.description = ''
        if (person.save()) {
            role.addToPeople(person)
            if (config.security.useMail) {
                String emailContent = """You have signed up for an account at:

 ${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}

 Here are the details of your account:
 -------------------------------------
 LoginName: ${person.username}
 Email: ${person.email}
 Full Name: ${person.firstName}
 Password: ${params.passwd}
"""

                def email = [
                    to: [person.email], // 'to' expects a List, NOT a single email address
                    subject: "[${request.contextPath}] Account Signed Up",
                    text: emailContent // 'text' is the email body
                ]
                emailerService.sendEmails([email])
            }

            person.save(flush: true)

            def auth = new AuthToken(person.username, params.passwd)
            def authtoken = daoAuthenticationProvider.authenticate(auth)
            SCH.context.authentication = authtoken
            if(redirectTo){
                redirect url: redirectTo
            } else {
                render template: 'thanks', model: [person: person]
            }
        }
        else {
            person.passwd = ''
            render template: 'index', model: [person: person]
        }
    }


}
