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

package com.nerderg.groupie

import grails.test.*
import com.nerderg.groupie.member.Member

class GUserTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGUser() {
        def existingUser = new GUser(username: 'fred', salutation: 'Mr', firstName: 'fred', lastName: 'smith', passwd: 'passwd', enabled: true )
        mockForConstraintsTests(GUser, [existingUser])
        //nullables
        def target = new GUser()
        assertFalse target.validate()
        println target.errors
        assertEquals "nullable", target.errors['username']
        assertEquals "nullable", target.errors['lastName']
        assertEquals "nullable", target.errors['firstName']
        assertEquals "nullable", target.errors['passwd']
        assertNull target.errors['email'] //can have null email
        assertNull target.errors['member'] //can have null member
        assertNull target.errors['salutation'] //can have null salutation
        //blanks not allowed except email
        target.username = ''
        target.firstName = ''
        target.lastName = ''
        target.passwd = ''
        target.email = ''
        target.member = new Member()
        assertFalse target.validate()
        println target.errors
        assertEquals "blank", target.errors['username']
        assertEquals "blank", target.errors['lastName']
        assertEquals "blank", target.errors['firstName']
        assertEquals "blank", target.errors['passwd']
        assertNull target.errors['email']
        //should validate
        target.username = 'freddo'
        target.firstName = 'Fred'
        target.lastName = 'Smith'
        target.passwd = 'passwd'
        target.email = 'fred@smith.com'
        assertTrue target.validate()
        println target.errors
        //bad email address
        target.email = 'fred@smith'
        assertFalse target.validate()
        println target.errors
        assertEquals "email", target.errors['email']
        //non unique username
        target.username = 'fred'
        target.email = 'fred@smith.com'
        assertFalse target.validate()
        println target.errors
        assertEquals "unique", target.errors['username']

        def chrs100 ='0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'
        def chrs101 ='01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'
        //should validate
        target.username = chrs100
        target.firstName = chrs100
        target.lastName = chrs100
        target.passwd = chrs100
        target.email = '012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789@smith.com' //100 chars
        target.salutation = '01234567890123456789' //20 chars
        assertTrue target.validate()
        println target.errors
        //bust the limits
        target.username = chrs101
        target.firstName = chrs101
        target.lastName = chrs101
        target.passwd = chrs101
        target.email = '012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789@1smith.com' //101 chars
        target.salutation = '012345678901234567890' //21 chars
        assertFalse target.validate()
        println target.errors
        assertEquals "maxSize", target.errors['username']
        assertEquals "maxSize", target.errors['lastName']
        assertEquals "maxSize", target.errors['firstName']
        assertEquals "maxSize", target.errors['passwd']
        assertEquals "maxSize", target.errors['email']
        assertEquals "maxSize", target.errors['salutation']
    }

    void testToName() {
        def user = new GUser(username: 'fred', salutation: 'Mr', firstName: 'fred', lastName: 'smith', passwd: 'passwd', enabled: true )
        println user.toName()
        assertEquals "fred smith", user.toName()

    }

    void testToString() {
        def user = new GUser(username: 'fred', salutation: 'Mr', firstName: 'fred', lastName: 'smith', passwd: 'passwd', enabled: true )
        println user
        assertEquals "fred - Mr fred smith", user.toString()
    }
}
