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
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
    <span class="menuButton">
      <g:link class="list" controller="mailingList" action="list">Mailing Lists</g:link>
    </span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="mailAccount.id.label" default="Id" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "id")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.name.label" default="Name" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "name")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.email.label" default="email" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "email")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.server.label" default="Server" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "server")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.username.label" default="Username" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "username")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.password.label" default="Password" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "password")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.port.label" default="Port" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "port")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.type.label" default="Type" /></td>

        <td valign="top" class="value">${fieldValue(bean: mailAccountInstance, field: "type")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="mailAccount.ssl.label" default="Ssl" /></td>

        <td valign="top" class="value"><g:formatBoolean boolean="${mailAccountInstance?.ssl}" /></td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${mailAccountInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
