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

<%@ page import="com.nerderg.groupie.mail.MailingList" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'mailingList.label', default: 'MailingList')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
    <span class="menuButton">
      <g:link class="list" controller="mailAccount" action="list">Mail Accounts</g:link>
    </span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${mailingListInstance}">
      <div class="errors">
        <g:renderErrors bean="${mailingListInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${mailingListInstance?.id}" />
      <g:hiddenField name="version" value="${mailingListInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="name"><g:message code="mailingList.name.label" default="Name" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: mailingListInstance, field: 'name', 'errors')}">
          <g:textField name="name" maxlength="100" value="${mailingListInstance?.name}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="rxMailServer"><g:message code="mailingList.rxMailServer.label" default="Rx Mail Server" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailingListInstance, field: 'rxMailServer', 'errors')}">
          <g:select name="rxMailServer.id" from="${com.nerderg.groupie.mail.MailAccount.list()}" optionKey="id" value="${mailingListInstance?.rxMailServer?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="users"><g:message code="mailingList.users.label" default="Users" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailingListInstance, field: 'users', 'errors')}">
          <g:select name="users" from="${com.nerderg.groupie.GUser.list()}" multiple="yes" optionKey="id" size="5" value="${mailingListInstance?.users}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="params"><g:message code="mailingList.params.label" default="Params" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailingListInstance, field: 'params', 'errors')}">

            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="txMailServer"><g:message code="mailingList.txMailServer.label" default="Tx Mail Server" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: mailingListInstance, field: 'txMailServer', 'errors')}">
          <g:select name="txMailServer.id" from="${com.nerderg.groupie.mail.MailAccount.list()}" optionKey="id" value="${mailingListInstance?.txMailServer?.id}"  />
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
