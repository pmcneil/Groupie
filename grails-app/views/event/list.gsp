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

<%@ page import="com.nerderg.groupie.event.Event" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'event.label', default: 'Event')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New event</g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'event.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'event.name.label', default: 'Name')}" />
                        
                            <th><g:message code="event.rsvps.label" default="RSVPs" /></th>

                            <g:sortableColumn property="location" title="${message(code: 'event.location.label', default: 'Location')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'event.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="startDate" title="${message(code: 'event.startDate.label', default: 'Start Date')}" />
                        
                            <th><g:message code="event.organiser.label" default="Organiser" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${eventInstanceList}" status="i" var="eventInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${eventInstance.id}">${fieldValue(bean: eventInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: eventInstance, field: "name")}</td>
                        
                            <td>${eventInstance.rsvp.size()}</td>

                            <td>${fieldValue(bean: eventInstance, field: "location")}</td>
                        
                            <td>${fieldValue(bean: eventInstance, field: "description")}</td>
                        
                            <td><g:formatDate date="${eventInstance.startDate}" /></td>
                        
                            <td>${fieldValue(bean: eventInstance, field: "organiser")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${eventInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
