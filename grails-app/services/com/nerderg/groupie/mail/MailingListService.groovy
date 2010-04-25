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

import javax.mail.Flags
import javax.mail.Message
import com.nerderg.groupie.GUser

class MailingListService {

    boolean transactional = true
    def mailReaderService

    def poll() {
        def mailingLists = MailingList.list()
        mailingLists.each {mlist ->
            log.debug "Check mail for $mlist.name"
            def rx = mlist.rxMailServer
            mailReaderService.receive(rx.server, rx.port.toInteger(), rx.type, rx.ssl, rx.username, rx.password) { Message msg ->
                log.debug "message $msg"
                if (!msg.isSet(Flags.Flag.SEEN) && !msg.isSet(Flags.Flag.ANSWERED)) {
                    checkFromRegisteredUser(msg, mlist)
                    mailReaderService.printMessage(msg)
                }
            }
        }
    }

    def checkFromRegisteredUser(Message msg, MailingList mlist) {
        def froms = msg.from
        if(froms && froms.size() == 1) {
            def from = froms[0]
            def user = GUser.findByEmail(from.address)
            if(!user){
                log.debug "message not from a registered user $from.address"
                return false
            }
            if(!mlist.users.contains(user)) {
                log.debug "message not from a mailing list user $from.address"
                return false
            }
        }
        return true
    }
}
