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

<%@ page import="com.nerderg.groupie.donate.Sponsorship" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Sponsorship</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Sponsorship List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Sponsorship</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Sponsorship</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sponsorshipInstance}">
            <div class="errors">
                <g:renderErrors bean="${sponsorshipInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${sponsorshipInstance?.id}" />
                <input type="hidden" name="version" value="${sponsorshipInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="endDate">End Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'endDate','errors')}">
                                    <g:datePicker name="endDate" value="${sponsorshipInstance?.endDate}" precision="minute" noSelection="['':'']"></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="frequency">Frequency:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'frequency','errors')}">
                                    <g:select id="frequency" name="frequency" from="${sponsorshipInstance.constraints.frequency.inList}" value="${sponsorshipInstance.frequency}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="donations">Donations:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'donations','errors')}">
                                    
<ul>
<g:each var="d" in="${sponsorshipInstance?.donations?}">
    <li><g:link controller="donation" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="donation" params="['sponsorship.id':sponsorshipInstance?.id]" action="create">Add Donation</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="member">Member:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'member','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.member.Member.list()}" name="member.id" value="${sponsorshipInstance?.member?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startDate">Start Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'startDate','errors')}">
                                    <g:datePicker name="startDate" value="${sponsorshipInstance?.startDate}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="target">Target:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorshipInstance,field:'target','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.donate.Target.list()}" name="target.id" value="${sponsorshipInstance?.target?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
