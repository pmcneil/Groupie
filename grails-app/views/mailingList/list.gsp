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
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
    <span class="menuButton">
      <g:link class="list" controller="mailAccount" action="list">Mail Accounts</g:link>
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

        <g:sortableColumn property="id" title="${message(code: 'mailingList.id.label', default: 'Id')}" />

        <g:sortableColumn property="name" title="${message(code: 'mailingList.name.label', default: 'Name')}" />

        <th><g:message code="mailingList.rxMailServer.label" default="Rx Mail Server" /></th>

        <g:sortableColumn property="params" title="${message(code: 'mailingList.params.label', default: 'Params')}" />

        <th><g:message code="mailingList.txMailServer.label" default="Tx Mail Server" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${mailingListInstanceList}" status="i" var="mailingListInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

            <td><g:link action="show" id="${mailingListInstance.id}">${fieldValue(bean: mailingListInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: mailingListInstance, field: "name")}</td>

          <td>${fieldValue(bean: mailingListInstance, field: "rxMailServer")}</td>

          <td>${fieldValue(bean: mailingListInstance, field: "params")}</td>

          <td>${fieldValue(bean: mailingListInstance, field: "txMailServer")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${mailingListInstanceTotal}" />
    </div>
  </div>
</body>
</html>
