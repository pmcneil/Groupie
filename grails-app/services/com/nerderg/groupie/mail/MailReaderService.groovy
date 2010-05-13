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

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

class MailReaderService {

    boolean transactional = false

    def receive(server, port, type, ssl, user, pass, closure) {
        def store
        def folder
        def storeType = ssl ? type + 's' : type

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

            folder.open(Folder.READ_WRITE)
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

    def send(server, port, ssl, user, pass, details, content, contentType, addresses) {
 	Properties props = new Properties()
        props.put("mail.transport.protocol", "smtp");
    	props.put("mail.host", server);
    	props.put("mail.smtp.socketFactory.port", port);
        if(ssl) {
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        }
    	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.quitwait", "false");

        log.debug "$props"

	Session session = Session.getInstance(props, new GroupieAuthenticator(user, pass));
        MimeMessage message = new MimeMessage(session)
        details.headers.each {
            message.setHeader(it.key, it.value)
        }
        message.setFrom(details.from)
        message.setRecipients(Message.RecipientType.TO, details.to.toArray() as Address[])
        message.setSubject(details.subject)
        message.setContent(content, contentType)
        try {
            log.debug "Sending emails"
            Transport.send(message, addresses.toArray() as Address[])
        } catch(e) {
            log.error(e)
        }
    }
}
