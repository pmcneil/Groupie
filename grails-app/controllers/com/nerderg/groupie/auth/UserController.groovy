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
import com.nerderg.groupie.mail.MailingList
import org.codehaus.groovy.grails.plugins.springsecurity.Secured
import com.nerderg.groupie.BaseController

/**
 * User controller.
 */

@Secured(['ROLE_ADMIN'])
class UserController extends BaseController {

    def authenticateService
    def memberService
    
    // the delete, save and update actions only accept POST requests
    static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

    def index = {
        redirect action: list, params: params
    }

    def list = {
        if (!params.max) {
            params.max = 10
        }
        [personList: GUser.list(params)]
    }

    def manage = {
        def userid = params.id
        if(!userid){
            userid = session.getAttribute('lastEditedUserId');
        }
        def userInstance = GUser.get( userid )

        ['person': userInstance]
    }

    def eip = {
        def userInstance = GUser.get( params.id )
        session.setAttribute('lastEditedUserId', params.id )
        renderJson(g.render(template: 'editInPlace', model: ['user':userInstance]))
    }

    def suggest = {
        log.debug "member suggest $params.q"
        HashSet suggestions = new HashSet()
        suggestions.addAll(GUser.findAllByFirstNameLike("%$params.q%"))
        suggestions.addAll(GUser.findAllByLastNameLike("%$params.q%"))
        suggestions.addAll(GUser.findAllByUsernameLike("%$params.q%"))
        suggestions.addAll(GUser.findAllByEmailLike("%$params.q%"))
        log.debug "suggestions $suggestions"
        render(template: 'suggest', model: ['results': suggestions.toList()])
    }

    def makeUserMember ={
        def userInstance = GUser.get( params.id )
        memberService.makeBasicMember(userInstance)
        redirect action: manage, params: params
    }

    def show = {
        def person = GUser.get(params.id)
        if (!person) {
            flash.message = "User not found with id $params.id"
            redirect action: list
            return
        }
        List roleNames = []
        for (role in person.authorities) {
            roleNames << role.authority
        }
        roleNames.sort { n1, n2 ->
            n1 <=> n2
        }
        [person: person, roleNames: roleNames]
    }

    /**
     * Person delete action. Before removing an existing person,
     * he should be removed from those authorities which he is involved.
     */
    def delete = {

        def person = GUser.get(params.id)
        if (person) {
            def authPrincipal = authenticateService.principal()
            //avoid self-delete if the logged-in user is an admin
            if (!(authPrincipal instanceof String) && authPrincipal.username == person.username) {
                flash.message = "You can not delete yourself, please login as another admin and try again"
            }
            else {
                //first, delete this person from People_Authorities table.
                Role.findAll().each { it.removeFromPeople(person) }
                person.delete()
                flash.message = "User $params.id deleted."
            }
        }
        else {
            flash.message = "User not found with id $params.id"
        }

        redirect action: list
    }

    def edit = {

        def person = GUser.get(params.id)
        if (!person) {
            flash.message = "User not found with id $params.id"
            redirect action: list
            return
        }

        return buildPersonModel(person)
    }

    /**
     * Person update action.
     */
    def update = {

        def person = GUser.get(params.id)
        if (!person) {
            flash.message = "User not found with id $params.id"
            redirect action: edit, id: params.id
            return
        }

        long version = params.version.toLong()
        if (person.version > version) {
            person.errors.rejectValue 'version', "person.optimistic.locking.failure",
				"Another user has updated this User while you were editing."
            render view: 'edit', model: buildPersonModel(person)
            return
        }

        def oldPassword = person.passwd
        person.properties = params
        if (!params.passwd.equals(oldPassword)) {
            person.passwd = authenticateService.encodePassword(params.passwd)
        }
        if (person.save()) {
            Role.findAll().each { it.removeFromPeople(person) }
            addRoles(person)
            redirect action: show, id: person.id
        }
        else {
            render view: 'edit', model: buildPersonModel(person)
        }
    }

    def ajaxUpdate = {

        log.debug "Ajax Update user with $params"
        def person = GUser.get(params.id)
        if (!person) {
            renderJson(errors: "<div class='errors'>User not found with id ${params.id}</div>")
            return
        }

        long version = params.version.toLong()
        if (person.version > version) {
            person.errors.rejectValue 'version', "person.optimistic.locking.failure",
				"Another user has updated this User while you were editing."
            renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: person]) )
            return
        }

        def oldPassword = person.passwd
        person.properties = params
        if (params.passwd && !params.passwd.equals(oldPassword)) {
            log.debug "*** Changing password to $params.passwd"
            person.passwd = authenticateService.encodePassword(params.passwd)
        } else {
            person.passwd = oldPassword
        }
            person.mailingLists.clear()
            addMailingLists(person)
        if (person.save()) {
            Role.findAll().each { it.removeFromPeople(person) }
            addRoles(person)
            renderJson([ok: "<div  class='message'> User updated</div>"])
        } else {
            renderJson(errors: g.render(template: '/ajaxerrors', model: [inst: person]) )
        }
    }

    def addMailingLists(person) {
        log.debug "in addMailingLists with params $params"
        for (String key in params.keySet()) {
            if(key.startsWith('mlist-')) {
                def id = key.substring(6).toLong()
                def mlist = MailingList.get(id)
                person.addToMailingLists(mlist)
                log.debug "added $person to $mlist.name"
            }
        }
    }

    def create = {
        def person = new GUser(params)
        render view: 'create', model: buildPersonModel(person)
    }

    /**
     * Person save action.
     */
    def save = {

        def person = new GUser()
        person.properties = params
        person.passwd = authenticateService.encodePassword(params.passwd)
        if (person.save()) {
            addRoles(person)
            redirect action: show, id: person.id
        }
        else {
            render view: 'create', model: [authorityList: Role.list(), person: person]
        }
    }

    private void addRoles(person) {
        for (String key in params.keySet()) {
            if (key.contains('ROLE') && 'on' == params.get(key)) {
                Role.findByAuthority(key).addToPeople(person)
            }
        }
    }

    private Map buildPersonModel(person) {

        List roles = Role.list()
        roles.sort { r1, r2 ->
            r1.authority <=> r2.authority
        }
        Set userRoleNames = []
        for (role in person.authorities) {
            userRoleNames << role.authority
        }
        LinkedHashMap<Role, Boolean> roleMap = [:]
        for (role in roles) {
            roleMap[(role)] = userRoleNames.contains(role.authority)
        }

        return [person: person, roleMap: roleMap]
    }
}
