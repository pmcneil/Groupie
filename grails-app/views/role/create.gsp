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
  <title>Create Role</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
    <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
    <span class="menuButton"><g:link class="list" action="list">Role List</g:link></span>
  </div>

  <div class="body">

    <h1>Create Role</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${authority}">
      <div class="errors">
        <g:renderErrors bean="${authority}" as="list" />
      </div>
    </g:hasErrors>

    <g:form action="save">
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name"><label for="authority">Role Name:</label></td>
              <td valign="top" class="value ${hasErrors(bean:authority,field:'authority','errors')}">
                <input type="text" id="authority" name="authority" value="${authority?.authority?.encodeAsHTML()}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name"><label for="description">Description:</label></td>
              <td valign="top" class="value ${hasErrors(bean:authority,field:'description','errors')}">
                <input type="text" id="description" name="description" value="${authority?.description?.encodeAsHTML()}"/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="buttons">
        <span class="button"><input class="save" type="submit" value="Create" /></span>
      </div>
    </g:form>
  </div>
</body>
