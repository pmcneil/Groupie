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

<%@ page import="com.nerderg.groupie.content.Content" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'editpage.css')}" />

        <title>Edit Content</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Page List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Page</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Content</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${contentInstance}">
            <div class="errors">
                <g:renderErrors bean="${contentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post">
                <input type="hidden" name="id" value="${contentInstance?.id}" />
                <input type="hidden" name="version" value="${contentInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="author">Author:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'author','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.GUser.list()}" name="author.id" value="${contentInstance?.author?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="content">Content:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'content','errors')}">
                                  <textarea cols="80" rows="20" id="content" name="content" ><groupie:displayContent content="${contentInstance}"/></textarea>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateCreated">Date Created:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'dateCreated','errors')}">
                                    <g:datePicker name="dateCreated" value="${contentInstance?.dateCreated}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="item">Item:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'item','errors')}">
                                    <g:select optionKey="id" from="${com.nerderg.groupie.content.Item.list()}" name="item.id" value="${contentInstance?.item?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastUpdated">Last Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'lastUpdated','errors')}">
                                    <g:datePicker name="lastUpdated" value="${contentInstance?.lastUpdated}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="mimeType">Mime Type:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'mimeType','errors')}">
                                    <input type="text" id="mimeType" name="mimeType" value="${fieldValue(bean:contentInstance,field:'mimeType')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="title">Title:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:contentInstance,field:'title','errors')}">
                                    <input type="text" id="title" name="title" value="${fieldValue(bean:contentInstance,field:'title')}"/>
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
