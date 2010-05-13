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

<%@ page import="com.nerderg.groupie.mail.MailAccount" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'mailAccount.label', default: 'MailAccount')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
    <span class="menuButton">
      <g:link class="list" controller="mailingList" action="list">Mailing Lists</g:link>
    </span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn property="id" title="${message(code: 'mailAccount.id.label', default: 'Id')}" />

        <g:sortableColumn property="name" title="${message(code: 'mailAccount.name.label', default: 'Name')}" />

        <g:sortableColumn property="email" title="${message(code: 'mailAccount.email.label', default: 'email')}" />

        <g:sortableColumn property="server" title="${message(code: 'mailAccount.server.label', default: 'Server')}" />

        <g:sortableColumn property="username" title="${message(code: 'mailAccount.username.label', default: 'Username')}" />

        <g:sortableColumn property="password" title="${message(code: 'mailAccount.password.label', default: 'Password')}" />

        <g:sortableColumn property="port" title="${message(code: 'mailAccount.port.label', default: 'Port')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${mailAccountInstanceList}" status="i" var="mailAccountInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

            <td><g:link action="show" id="${mailAccountInstance.id}">${fieldValue(bean: mailAccountInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: mailAccountInstance, field: "name")}</td>

          <td>${fieldValue(bean: mailAccountInstance, field: "email")}</td>

          <td>${fieldValue(bean: mailAccountInstance, field: "server")}</td>

          <td>${fieldValue(bean: mailAccountInstance, field: "username")}</td>

          <td>${fieldValue(bean: mailAccountInstance, field: "password")}</td>

          <td>${fieldValue(bean: mailAccountInstance, field: "port")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${mailAccountInstanceTotal}" />
    </div>
  </div>
</body>
</html>
