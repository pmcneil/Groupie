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

import grails.test.*
import com.nerderg.groupie.content.*
import com.nerderg.groupie.*

class ChildFlowIntergrationTests extends WebFlowTestCase {

    def sponsorController = new SponsorController()
    def authenticateService
    
    protected void setUp() {
        super.setUp()
        createBaseRolesIfRequired()
        createAdminUserIfRequired()
        createEditorUserIfRequired()
        def target = createContent('fred', 'content', 'fred', 'text/html',
"""
Fred age 11
""")
        assert target
        tagContent("Sponsor Me", target)
    }

    protected void tearDown() {
        super.tearDown()
    }

    def getFlow() {
        sponsorController.childFlow
    }

    void createBaseRolesIfRequired() {
        if(!Role.findByAuthority("ROLE_ADMIN")){
            def adminRole = new Role(authority: "ROLE_ADMIN", description: "Administrator")
            assert adminRole.save()
            def editorRole = new Role(authority: "ROLE_EDITOR", description: "Content editor")
            assert editorRole.save()
            def userRole = new Role(authority: "ROLE_USER", description: "Registered user")
            assert userRole.save()
        }
    }

    void createAdminUserIfRequired() {
        if(!GUser.findByUsername("admin")) {
            def user = new GUser(username: "admin", passwd: authenticateService.encodePassword("admin"),
                email: "admin@nerderg.com", firstName: "Peter", lastName: "McNeil", enabled: true)
            assert user.save()
            Role.findByAuthority("ROLE_ADMIN").addToPeople(user)
        } else {
            println "Existing admin user"
        }
    }

    void createEditorUserIfRequired() {
        if(!GUser.findByUsername("editor")) {
            def user = new GUser(username: "editor", passwd: authenticateService.encodePassword("editor"),
                email: "editor@nerderg.com", firstName: "Peter", lastName: "McNeil", enabled: true)
            assert user.save()
            Role.findByAuthority("ROLE_EDITOR").addToPeople(user)
        } else {
            println "Existing editor user"
        }
    }

    def Item createContent(name, type, title, mimetype, content) {
        def user = GUser.findByUsername("admin")
        def item = new Item(name: name, type: type)
        assert item.save()
        def cnt = new Content(mimeType: mimetype, title: title,
            content: content, author: user, item: item)
        assert cnt.save()
        item.current = cnt
        assert item.save()
        return item
    }

    def Item tagContent(name, item) {
        def tag = Tag.findByName(name)
        if(!tag){
            tag = new Tag(name: name)
        }
        assert tag.save()
        item.addToTags(tag)
    }


    void testSponsor() {
        startFlow()
        assertCurrentStateEquals "intro"
        signalEvent('next')
        assertCurrentStateEquals "selectChild"
        assertTrue getFlowScope().children.size == 1
        assertTrue getFlowScope().children[0].name == 'fred'

        signalEvent('next')
        assertCurrentStateEquals "selectChild"

        sponsorController.params.id = 1
        signalEvent('next')
        assertCurrentStateEquals "registerUser"
        sponsorController.params.username = 'fred'
        sponsorController.params.salutation = 'Mr'
        sponsorController.params.firstName = 'Fred'
        sponsorController.params.lastName = 'Smith'
        sponsorController.params.passwd = 'passwd'
        sponsorController.params.repasswd = 'passwd'
        sponsorController.params.email = 'fred@smith.com'

        signalEvent('next')
        assertCurrentStateEquals "membershipDetails"
        assertTrue getFlowScope().chosenone == getFlowScope().children[0]
    }
}
