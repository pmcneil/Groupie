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

<%@ page import="com.nerderg.groupie.member.MemberData" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create MemberData</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
      <span class="menuButton"><g:link class="list" action="list">MemberData List</g:link></span>
    </div>
    <div class="body">
      <h1>Create MemberData</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${memberDataInstance}">
        <div class="errors">
          <g:renderErrors bean="${memberDataInstance}" as="list" />
        </div>
      </g:hasErrors>
      <g:form action="save" method="post" >
        <div class="dialog">
          <table>
            <tbody>

              <tr class="prop">
                <td valign="top" class="name">
                  <label for="data">Data:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean:memberDataInstance,field:'data','errors')}">
                  <input type="text" id="data" name="data" value="${fieldValue(bean:memberDataInstance,field:'data')}"/>
                </td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">
                  <label for="member">Member:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean:memberDataInstance,field:'member','errors')}">
            <g:select optionKey="id" from="${com.nerderg.groupie.member.Member.list()}" name="member.id" value="${memberDataInstance?.member?.id}" ></g:select>
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="tag">Tag:</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean:memberDataInstance,field:'tag','errors')}">
            <g:select optionKey="id" from="${com.nerderg.groupie.member.DataTag.list()}" name="tag.id" value="${memberDataInstance?.tag?.id}" ></g:select>
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
</html>
