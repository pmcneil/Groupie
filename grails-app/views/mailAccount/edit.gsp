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
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${mailAccountInstance}">
      <div class="errors">
        <g:renderErrors bean="${mailAccountInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${mailAccountInstance?.id}" />
      <g:hiddenField name="version" value="${mailAccountInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="name"><g:message code="mailAccount.name.label" default="Name" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'name', 'errors')}">
          <g:textField name="name" maxlength="100" value="${mailAccountInstance?.name}" />
          </td>
          </tr>

          <tr class="prop">
              <td valign="top" class="name">
                <label for="email"><g:message code="mailAccount.email.label" default="email" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'name', 'errors')}">
                <g:textField name="email" maxlength="100" value="${mailAccountInstance?.email}" />
              </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="server"><g:message code="mailAccount.server.label" default="Server" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'server', 'errors')}">
          <g:textField name="server" maxlength="100" value="${mailAccountInstance?.server}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="username"><g:message code="mailAccount.username.label" default="Username" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'username', 'errors')}">
          <g:textField name="username" maxlength="100" value="${mailAccountInstance?.username}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="password"><g:message code="mailAccount.password.label" default="Password" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'password', 'errors')}">
          <g:textArea name="password" cols="40" rows="5" value="${mailAccountInstance?.password}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="port"><g:message code="mailAccount.port.label" default="Port" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'port', 'errors')}">
          <g:textField name="port" maxlength="5" value="${mailAccountInstance?.port}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="mailAccount.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'type', 'errors')}">
          <g:select name="type" from="${mailAccountInstance.constraints.type.inList}" value="${mailAccountInstance?.type}" valueMessagePrefix="mailAccount.type"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="ssl"><g:message code="mailAccount.ssl.label" default="Ssl" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailAccountInstance, field: 'ssl', 'errors')}">
          <g:checkBox name="ssl" value="${mailAccountInstance?.ssl}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
