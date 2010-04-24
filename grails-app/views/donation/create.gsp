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

<%@ page import="com.nerderg.groupie.donate.Donation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Donation</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Donation List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Donation</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${donationInstance}">
            <div class="errors">
                <g:renderErrors bean="${donationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="amount">Amount:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'amount','errors')}">
                                    <input type="text" id="amount" name="amount" value="${fieldValue(bean:donationInstance,field:'amount')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sponsorship">Sponsorship:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'sponsorship','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.donate.Sponsorship.list()}" name="sponsorship.id" value="${donationInstance?.sponsorship?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Create Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'createDate','errors')}">
                                    <g:datePicker name="createDate" value="${donationInstance?.createDate}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="member">Member:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'member','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.member.Member.list()}" name="member.id" value="${donationInstance?.member?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="paid">Paid:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'paid','errors')}">
                                    <g:checkBox name="paid" value="${donationInstance?.paid}" ></g:checkBox>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="target">Target:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:donationInstance,field:'target','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.donate.Target.list()}" name="target.id" value="${donationInstance?.target?.id}" ></g:select>
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
