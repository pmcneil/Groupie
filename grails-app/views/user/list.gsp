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
<head>
  <meta name="layout" content="main" />
  <title>User List</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
    <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
  </div>

  <div class="body">
    <h1>User List</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>
        <g:sortableColumn property="id" title="Id" />
        <g:sortableColumn property="username" title="Login Name" />
        <g:sortableColumn property="firstName" title="First Name" />
        <g:sortableColumn property="lastName" title="Last Name" />
        <g:sortableColumn property="enabled" title="Enabled" />
        <g:sortableColumn property="member" title="Member" />
        <g:sortableColumn property="description" title="Description" />
        <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${personList}" status="i" var="person">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${person.id}</td>
            <td>${person.username?.encodeAsHTML()}</td>
            <td>${person.firstName?.encodeAsHTML()}</td>
            <td>${person.lastName?.encodeAsHTML()}</td>
            <td>${person.enabled?.encodeAsHTML()}</td>
            <td>${person.member?.encodeAsHTML()}</td>
            <td>${person.description?.encodeAsHTML()}</td>
            <td class="actionButtons">
              <span class="actionButton">
                <g:link action="manage" id="${person.id}">Manage</g:link>
              </span>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="paginateButtons">
      <g:paginate total="${com.nerderg.groupie.GUser.count()}" />
    </div>

  </div>
</body>
