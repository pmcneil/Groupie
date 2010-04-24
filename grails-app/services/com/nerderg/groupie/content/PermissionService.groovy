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

package com.nerderg.groupie.content
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import com.nerderg.groupie.Requestmap

class PermissionService {

    boolean transactional = true
    def authenticateService


    def readPerms = ['Anyone' : [reqMap: 'IS_AUTHENTICATED_ANONYMOUSLY', 'default': true],
                     'Author': [reqMap: 'ROLE_ADMIN,ROLE_EDITOR', userIsAuthor: true],
                     'Editors': [reqMap: 'ROLE_ADMIN,ROLE_EDITOR'],
                     'Registered Users': [reqMap: 'ROLE_ADMIN,ROLE_EDITOR,ROLE_USER'],
                     'Admin Only': [reqMap: 'ROLE_ADMIN']
    ]

    def writePerms = ['Author': [reqMap: 'ROLE_ADMIN,ROLE_EDITOR', userIsAuthor: true],
                     'Editors': [reqMap: 'ROLE_ADMIN,ROLE_EDITOR'],
                     'Admin Only': [reqMap: 'ROLE_ADMIN']
    ]


    def isReadable(item) {
        def perm = readPerms[item.readPerms]
        if(!perm){
            perm = readPerms['Anyone']
        }
        return allowed(item.current?.author, perm)
    }

    def isWritable(item) {
        def perm = writePerms[item.writePerms]
        if(!perm){
            log.debug "setting default write perms"
            perm = writePerms['Editors']
        }
        return allowed(item.current?.author, perm)
    }

    def allowed(author, perm) {
        if(perm['default']){
            return true;
        }
        def inRole = authenticateService.ifAnyGranted(perm.reqMap)
        if(perm.userIsAuthor) {
            def user = authenticateService.userDomain()
            return authenticateService.ifAnyGranted('ROLE_ADMIN') || (inRole && (author.id == user.id))
        }
        return inRole
    }

    def updatePermissions(newPerms, url) {
        def perms = readPerms[newPerms]

        if(!perms){
            log.debug "*** Unknown permission newPerms, doing nothing ***"
            return //no such permission so do nothing
        }

        def cleanurl = url.toLowerCase().encodeAsURL()
        def requestmap = Requestmap.findByUrl("/$cleanurl")

        if(perms.reqMap){
            log.debug "updating requestmap for /$cleanurl to $perms.reqMap"
            if(requestmap){
                requestmap.configAttribute = perms.reqMap
            } else {
                requestmap = new Requestmap(url: "/$cleanurl", configAttribute: perms.reqMap)
            }
            if (save(requestmap)) {
                authenticateService.clearCachedRequestmaps()
            }
        } else if(requestmap){
            log.debug "deleting requestmap for /$cleanurl"
            requestmap.delete()
            authenticateService.clearCachedRequestmaps()
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
}
