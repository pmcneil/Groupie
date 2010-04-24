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
    <title>Edit Address</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="list" action="list">Address List</g:link></span>
      <span class="menuButton"><g:link class="create" action="create">New Address</g:link></span>
    </div>
    <div class="body">
      <h1>Edit Address</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${addressInstance}">
        <div class="errors">
          <g:renderErrors bean="${addressInstance}" as="list" />
        </div>
      </g:hasErrors>
      <g:form method="post" >
        <input type="hidden" name="id" value="${addressInstance?.id}" />
        <input type="hidden" name="version" value="${addressInstance?.version}" />
        <div class="dialog">
          <g:render template="address_form" model="['addressInstance':addressInstance]"/>
          <nerderg:formfield label="Member" field="member" bean="${addressInstance}">
            <g:select optionKey="id" from="${com.nerderg.groupie.member.Member.list()}" name="member.id" value="${addressInstance?.member?.id}" ></g:select>
          </nerderg:formfield>

        </div>
        <div class="buttons">
          <span class="button"><g:actionSubmit class="save" value="Update" /></span>
          <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
