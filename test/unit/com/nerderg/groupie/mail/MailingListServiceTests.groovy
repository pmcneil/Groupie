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

package com.nerderg.groupie.mail

import com.nerderg.MailReaderService
import grails.test.*

class MockMessage {
    String from
    String subject
    String contentType
    String content

    def getInputStream() {
        new StringBufferInputStream(content)
    }
    def isSet(thing) {
        return false;
    }
}

class MailingListServiceTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
        mockLogging(MailingListService)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testPoll() {
        def mailAccnt = new MailAccount(name: 'maTest', server: 'mail.abc.com',
                                                 username: 'tester', password: 'password',
                                                 port: '123', type: 'imap', ssl: true)
        mockDomain(MailingList, [new MailingList(name: 'testList', rxMailServer: mailAccnt, txMailServer: null)])
        def readerControl = mockFor(MailReaderService)
        readerControl.demand.receive(1..1) { server, port, type, ssl, user, pass, closure ->
            closure(new MockMessage(from: 'test@test.com', subject: 'Hello Test', contentType: 'text/plain', content: 'this is a message'))
        }
        readerControl.demand.printMessage(1..1){msg ->
            assertEquals "test@test.com", msg.from
        }
        def testMLService = new MailingListService()
        testMLService.mailReaderService = readerControl.createMock()
        testMLService.poll()
        readerControl.verify()
    }
}
