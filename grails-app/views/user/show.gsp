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
  <title>Show User</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
    <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
    <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
  </div>

  <div class="body">
    <h1>Show User</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">ID:</td>
            <td valign="top" class="value">${person.id}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Login Name:</td>
            <td valign="top" class="value">${person.username?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">First Name:</td>
            <td valign="top" class="value">${person.firstName?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Last Name:</td>
            <td valign="top" class="value">${person.lastName?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Enabled:</td>
            <td valign="top" class="value">${person.enabled}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Description:</td>
            <td valign="top" class="value">${person.description?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Email:</td>
            <td valign="top" class="value">${person.email?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Show Email:</td>
            <td valign="top" class="value">${person.emailShow}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Member:</td>
            <td valign="top" class="value">${person.member}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Roles:</td>
            <td valign="top" class="value">
              <ul>
                <g:each in="${roleNames}" var='name'>
                  <li>${name}</li>
                </g:each>
              </ul>
            </td>
          </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <input type="hidden" name="id" value="${person.id}" />
        <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
        <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
      </g:form>
    </div>

  </div>
</body>
