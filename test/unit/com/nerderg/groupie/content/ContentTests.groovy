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

import grails.test.*
import com.nerderg.groupie.GUser

class ContentTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testContent() {
        mockForConstraintsTests(Content)
        //nullables
        def target = new Content()
        assertFalse target.validate()
        println target.errors
        assertEquals "nullable", target.errors['title']
        assertEquals "nullable", target.errors['mimeType']
        assertEquals "nullable", target.errors['content']
        assertEquals "nullable", target.errors['author']
        assertEquals "nullable", target.errors['item']
        //should validate
        target.title = 'test content'
        target.mimeType = 'text/plain'
        target.content = 'test content'
        target.item = new Item()
        target.author = new GUser(username: 'fred', salutation: 'Mr', firstName: 'fred', lastName: 'smith', passwd: 'passwd', enabled: true )
        assertTrue target.validate()
        println target.errors
        //max limits of 100 - won't test 1M content limit
        target.title = '0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'
        target.mimeType = '0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'
        assertTrue target.validate()
        println target.errors
        //blanks OK
        target.title = ''
        target.mimeType = ''
        target.content = ''
         assertTrue target.validate()
        println target.errors
        //exceed size by one
        target.title = '01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'
        target.mimeType = '01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'
        assertFalse target.validate()
        println target.errors
        assertEquals "maxSize", target.errors['title']
        assertEquals "maxSize", target.errors['mimeType']
    }
}
