<%--
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
--%>

<%@ page import="com.nerderg.groupie.event.Rsvp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'rsvp.label', default: 'Rsvp')}" />
        <title>List RSVPs</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New RSVP</g:link></span>
        </div>
        <div class="body">
            <h1>List RSVPs</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'rsvp.id.label', default: 'Id')}" />
                        
                            <th><g:message code="rsvp.event.label" default="Event" /></th>
                   	    
                            <g:sortableColumn property="coming" title="${message(code: 'rsvp.coming.label', default: 'Coming')}" />
                        
                            <th><g:message code="rsvp.user.label" default="User" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${rsvpInstanceList}" status="i" var="rsvpInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${rsvpInstance.id}">${fieldValue(bean: rsvpInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: rsvpInstance, field: "event")}</td>
                        
                            <td><g:formatBoolean boolean="${rsvpInstance.coming}" /></td>
                        
                            <td>${fieldValue(bean: rsvpInstance, field: "user")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${rsvpInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
