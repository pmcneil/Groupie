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

<%@ page import="com.nerderg.groupie.member.Address" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Address</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Address List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Address</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${addressInstance}">
            <div class="errors">
                <g:renderErrors bean="${addressInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address">Address:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:addressInstance,field:'address','errors')}">
                                  <textarea id="address" name="address" cols="40" rows="2"
                                              value="${fieldValue(bean:addressInstance,field:'address')}">
                                       ${fieldValue(bean:addressInstance,field:'address')}
                                    </textarea>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postcode">Postcode:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:addressInstance,field:'postcode','errors')}">
                                    <input type="text" id="postcode" name="postcode" value="${fieldValue(bean:addressInstance,field:'postcode')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:addressInstance,field:'state','errors')}">
                                    <g:select id="state" name="state" from="${addressInstance.constraints.state.inList}" value="${addressInstance.state}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country">Country:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:addressInstance,field:'country','errors')}">
                                    <g:select id="country" name="country" from="${addressInstance.constraints.country.inList}" value="${addressInstance.country}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="member">Member:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:addressInstance,field:'member','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.member.Member.list()}" name="member.id" value="${addressInstance?.member?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
