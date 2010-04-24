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
  <title>Show Role</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
    <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
    <span class="menuButton"><g:link class="list" action="list">Role List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New Role</g:link></span>
  </div>

  <div class="body">
    <h1>Show Role</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">ID:</td>
            <td valign="top" class="value">${authority.id}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Role Name:</td>
            <td valign="top" class="value">${authority.authority}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Description:</td>
            <td valign="top" class="value">${authority.description}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">People:</td>
            <td valign="top" class="value">${authority.people}</td>
          </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <input type="hidden" name="id" value="${authority?.id}" />
        <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
        <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
      </g:form>
    </div>

  </div>

</body>
