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

<%@ page import="com.nerderg.groupie.content.Layout" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Layout List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Layout</g:link></span>
        </div>
        <div class="body">
            <h1>Layout List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="name" title="Name" />
                        
                   	        <g:sortableColumn property="fixed" title="Fixed" />
                        
                   	        <g:sortableColumn property="width" title="Width" />
                        
                   	        <g:sortableColumn property="margin" title="Margin" />
                        
                   	        <g:sortableColumn property="sidebar" title="Sidebar" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${layoutInstanceList}" status="i" var="layoutInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${layoutInstance.id}">${fieldValue(bean:layoutInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:layoutInstance, field:'name')}</td>
                        
                            <td>${fieldValue(bean:layoutInstance, field:'fixed')}</td>
                        
                            <td>${fieldValue(bean:layoutInstance, field:'width')}</td>
                        
                            <td>${fieldValue(bean:layoutInstance, field:'margin')}</td>
                        
                            <td>${fieldValue(bean:layoutInstance, field:'sidebar')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${layoutInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
