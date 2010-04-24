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
        <title>New RSVP</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">List RSVPs</g:link></span>
        </div>
        <div class="body">
            <h1>New RSVP</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${rsvpInstance}">
            <div class="errors">
                <g:renderErrors bean="${rsvpInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="event"><g:message code="rsvp.event.label" default="Event" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: rsvpInstance, field: 'event', 'errors')}">
                                    <g:select name="event.id" from="${com.nerderg.groupie.event.Event.list()}" optionKey="id" value="${rsvpInstance?.event?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="user"><g:message code="rsvp.user.label" default="User" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: rsvpInstance, field: 'user', 'errors')}">
                                    <g:select name="user.id" from="${com.nerderg.groupie.GUser.list()}" optionKey="id" value="${rsvpInstance?.user?.id}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="coming"><g:message code="rsvp.coming.label" default="Coming" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: rsvpInstance, field: 'coming', 'errors')}">
                                    <g:checkBox name="coming" value="${rsvpInstance?.coming}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
