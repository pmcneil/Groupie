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

import com.nerderg.groupie.GUser
import com.nerderg.groupie.Role
import org.springframework.security.providers.UsernamePasswordAuthenticationToken as AuthToken
import org.springframework.security.context.SecurityContextHolder as SCH

class MemberService {

    def authenticateService
    def daoAuthenticationProvider

    boolean transactional = true

    def getMember() {
        if(authenticateService.isLoggedIn()){
            //authService.userDomain doesn't reference member for some reason
            def userName = authenticateService.principal().getUsername()
            if(userName){
                def user = GUser.findByUsername(userName)
                if(user) {
                    def m = user.member
                    log.debug "member is $m"
                    return m
                }
            }
        } else {
            return null
        }
    }

    def makeBasicMember(user) {
        if(!user){
            log.debug "user is null"
            return null
        }
        def memberInstance = new Member()
        memberInstance.user = user
        memberInstance.date = new Date()
        memberInstance.expiryDate = new Date().plus(365)

        def type = MemberType.findByName('Full')
        memberInstance.memberType = type

        if(save(memberInstance)) {
            user.member = memberInstance
            save(user, true)
            log.debug "--- Member ${memberInstance.id} created. User is $memberInstance.user with member $memberInstance.user.member"
            return memberInstance
        }

    }

    def makeMember(userCommand, memberCommand) {
        log.debug "userCmd $userCommand memberCmd $memberCommand"
        def user = authenticateService.isLoggedIn() ? GUser.findByUsername(authenticateService.principal().getUsername()) : createPerson(userCommand)
        if(user && user.member){
            return user.member
        }
        return createMember(user, memberCommand)
    }

    private Member createMember(user, memberCommand) {
        if(!user){
            log.debug "user is null"
            return null
        }
        def memberInstance = new Member()
        def addressInstance = new Address()
        def phoneInstance = new Phone()
        phoneInstance.number = memberCommand.contactPhone
        phoneInstance.type = memberCommand.phoneType
        phoneInstance.note = memberCommand.phoneNote

        addressInstance.address = memberCommand.address
        addressInstance.state = memberCommand.state
        addressInstance.country = memberCommand.country
        addressInstance.postcode = memberCommand.postcode

        memberInstance.user = user
        memberInstance.date = new Date()
        memberInstance.expiryDate = new Date().plus(365)

        def type = MemberType.findByName('Full')
        memberInstance.memberType = type

        if(save(memberInstance)) {
            user.member = memberInstance
            addressInstance.member = memberInstance
            phoneInstance.member = memberInstance

            save(user, true)

            log.debug "--- Member ${memberInstance.id} created. User is $memberInstance.user with member $memberInstance.user.member"

            if(save(addressInstance)) {
                memberInstance.addToMemberAddress(addressInstance)
            } else {
                log.debug "--- address fail **"
                return null
            }
            if(save(phoneInstance)) {
                memberInstance.addToMemberPhone(phoneInstance)
            } else {
                log.debug "--- phone fail **"
                return null
            }
        } else {
            log.debug "--- member fail **"
            return null
        }

    }

    private GUser createPerson(userCommand) {
        def person = new GUser()

        person.username = userCommand.username
        person.salutation = userCommand.salutation
        person.firstName = userCommand.firstName
        person.lastName = userCommand.lastName
        person.passwd = authenticateService.encodePassword(userCommand.passwd.trim())
        person.email = userCommand.email

        def config = authenticateService.securityConfig
        def defaultRole = config.security.defaultRole

        def role = Role.findByAuthority(defaultRole)
        if (!role) {
            log.debug "--- role fail **"
            return null
        }

        person.enabled = true
        person.emailShow = true
        person.description = 'Registered User'
        if (save(person, true)) {
            role.addToPeople(person)
            //            person.save(flush: true)

            def auth = new AuthToken(person.username, userCommand.passwd)
            def authtoken = daoAuthenticationProvider.authenticate(auth)
            SCH.context.authentication = authtoken
            return person
        } else {
            log.debug "--- user save fail **"
            return null
        }
    }

    def save(item){
        save(item, false)
    }

    def save(item, flsh) {
        if (item.save(flush: flsh)) {
            if(item.hasErrors()){
                log.debug "*** Errors Saving ***"
                item.errors.allErrors.each { log.debug it }
                return false
            }
            log.debug("$item saved")
            return true
        } else {
            log.debug "*** Errors Saving ***"
            item.errors.allErrors.each { log.debug it }
            return false
        }
    }
    
    public static Map buildUserModel(person) {

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
