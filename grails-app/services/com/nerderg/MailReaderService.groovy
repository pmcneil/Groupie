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

package com.nerderg
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

class MailReaderService {

    boolean transactional = false

    def receive(server, port, type, ssl, user, pass, closure) {
        def store
        def folder
        def storeType = type + ssl ? 's' : ''

        log.debug "Polling $user $server"
        try {
            Properties mailprops = new Properties(System.properties)
            mailprops.put("mail.debug", true)
            Session session = Session.getDefaultInstance(mailprops, null)
            store = session.getStore(storeType)
            log.debug "about to connect"
            store.connect(server, port, user, pass)
            log.debug "getting default folder"
            folder = store.getDefaultFolder()
            if (folder == null) {
                log.debug "No default folder"
                return
            }

            folder = folder.getFolder("INBOX")
            if (folder == null) {
                log.debug "No INBOX"
                return
            }

            folder.open(Folder.READ_ONLY)
            log.debug "getting Messages"
            folder.getMessages().each(closure)

        } catch (e) {
            e.printStackTrace()
        } finally {
            folder?.close(false)
            store?.close()
        }
    }

    def printMessage(Message message) {
        // Get the header information
        def froms = message.from
        froms.each { from ->
            log.debug "From: $from.personal <$from.address>"
        }
        log.debug "Subject: $message.subject"

        log.debug "Type: $message.contentType"

        if(message.contentType.startsWith("text/plain")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(message.inputStream))
            reader.readLines().each {
                log.debug it
            }
        }

        log.debug("--->");
    }
}
