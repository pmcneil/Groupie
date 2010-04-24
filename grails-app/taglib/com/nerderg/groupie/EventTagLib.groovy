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

import com.nerderg.groupie.content.*
import org.codehaus.groovy.grails.web.pages.*
import groovy.text.Template
import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools
import org.codehaus.groovy.grails.web.servlet.mvc.*
import com.nerderg.groupie.member.*
import com.nerderg.groupie.event.*

class EventTagLib {

    static namespace = "event"

    def authenticateService

    def hasNotRsvped = { attrs, body ->
        GUser user = authenticateService.userDomain()
        Event event = attrs.event
        Rsvp rsvp = Rsvp.findWhere(user: user, event: event)
        log.debug "tag hasNotRsvped $user, $event, $rsvp"
        if(!rsvp) {
            out << body()
        }
    }

    def mapFromLocation = {attrs ->
        Event event = attrs.event
        def location = event.location.substring(event.location.indexOf(',')+1)
        def encloc = "$location".trim().encodeAsURL()
        def url = "http://maps.google.com.au/maps?f=q&z=18&q=$encloc"
        out << "<a href='$url' target='gmap'>map</a>"
    }

}
