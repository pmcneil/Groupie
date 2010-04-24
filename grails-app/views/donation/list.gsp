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
        <title>Donation List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Donation</g:link></span>
        </div>
        <div class="body">
            <h1>Donation List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="amount" title="Amount" />
                        
                   	        <th>Sponsorship</th>
                   	    
                   	        <g:sortableColumn property="createDate" title="Create Date" />
                        
                   	        <th>Member</th>
                   	    
                   	        <g:sortableColumn property="paid" title="Paid" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${donationInstanceList}" status="i" var="donationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${donationInstance.id}">${fieldValue(bean:donationInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:donationInstance, field:'amount')}</td>
                        
                            <td>${fieldValue(bean:donationInstance, field:'sponsorship')}</td>
                        
                            <td>${fieldValue(bean:donationInstance, field:'createDate')}</td>
                        
                            <td>${fieldValue(bean:donationInstance, field:'member')}</td>
                        
                            <td>${fieldValue(bean:donationInstance, field:'paid')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${donationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
