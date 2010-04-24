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
    <title>Create Member</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="list" action="list">Member List</g:link></span>
    </div>
    <div class="body">
      <h1>Create Member</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${memberInstance}">
        <div class="errors">
          <g:renderErrors bean="${memberInstance}" as="list" />
        </div>
      </g:hasErrors>
      <g:form action="save" method="post" >
        <div class="dialog">

          <nerderg:formfield label="User"  bean="${memberInstance}" field="user">
            <g:select optionKey="id" from="${com.nerderg.groupie.GUser.list()}" name="user.id" value="${memberInstance?.user?.id}" ></g:select>
          </nerderg:formfield>

          <nerderg:formfield label="Date"  bean="${memberInstance}" field="date">
            <g:datePicker name="date" value="${memberInstance?.date}" precision="minute" ></g:datePicker>
          </nerderg:formfield>

          <nerderg:formfield label="Expiry Date"  bean="${memberInstance}" field="expiryDate">
            <g:datePicker name="expiryDate" value="${memberInstance?.expiryDate}" precision="minute" ></g:datePicker>
          </nerderg:formfield>

          <nerderg:formfield label="Member Type"  bean="${memberInstance}" field="memberType">
            <g:select optionKey="id" from="${com.nerderg.groupie.member.MemberType.list()}" name="memberType.id" value="${memberInstance?.memberType?.id}" ></g:select>
          </nerderg:formfield>

        </div>
        <div class="buttons">
          <span class="button"><input class="save" type="submit" value="Create" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
