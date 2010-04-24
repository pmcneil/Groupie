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

<%@ page import="com.nerderg.groupie.member.Member" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Member</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Member List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Member</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Member</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${memberInstance}">
            <div class="errors">
                <g:renderErrors bean="${memberInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${memberInstance?.id}" />
                <input type="hidden" name="version" value="${memberInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="user">User:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'user','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.GUser.list()}" name="user.id" value="${memberInstance?.user?.id}" ></g:select>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date">Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'date','errors')}">
                                    <g:datePicker name="date" value="${memberInstance?.date}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expiryDate">Expiry Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'expiryDate','errors')}">
                                    <g:datePicker name="expiryDate" value="${memberInstance?.expiryDate}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="memberAddress">Member Address:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'memberAddress','errors')}">
                                    
<ul>
<g:each var="m" in="${memberInstance?.memberAddress?}">
    <li><g:link controller="address" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="address" params="['member.id':memberInstance?.id]" action="create">Add Address</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="memberData">Member Data:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'memberData','errors')}">
                                    
<ul>
<g:each var="m" in="${memberInstance?.memberData?}">
    <li><g:link controller="memberData" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="memberData" params="['member.id':memberInstance?.id]" action="create">Add MemberData</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="memberFlags">Member Flags:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'memberFlags','errors')}">
                                    <g:select name="memberFlags"
from="${com.nerderg.groupie.member.MemberFlags.list()}"
size="5" multiple="yes" optionKey="id"
value="${memberInstance?.memberFlags}" />

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="memberType">Member Type:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:memberInstance,field:'memberType','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.member.MemberType.list()}" name="memberType.id" value="${memberInstance?.memberType?.id}" ></g:select>
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
