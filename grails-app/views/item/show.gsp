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

<%@ page import="com.nerderg.groupie.content.Item" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Show Item</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="list" action="list">Item List</g:link></span>
      <span class="menuButton"><g:link class="create" action="create">New Item</g:link></span>
    </div>
    <div class="body">
      <h1>Show Item</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="dialog">
        <table>
          <tbody>


            <tr class="prop">
              <td valign="top" class="name">Id:</td>

              <td valign="top" class="value">${fieldValue(bean:itemInstance, field:'id')}</td>

            </tr>

            <tr class="prop">
              <td valign="top" class="name">Type:</td>

              <td valign="top" class="value">${fieldValue(bean:itemInstance, field:'type')}</td>

            </tr>

            <tr class="prop">
              <td valign="top" class="name">Current:</td>

              <td valign="top" class="value">
                <g:if test="${itemInstance.current}">
                  <g:link controller="content" action="show" id="${itemInstance?.current?.id}">

                    &quot;${itemInstance?.current?.title.encodeAsHTML()}&quot;
                    edited <prettytime:display date="${itemInstance?.current?.lastUpdated}"/>
                    by ${itemInstance?.current?.author?.username.encodeAsHTML()}
                  </g:link>
                </g:if>
          <g:else>
            <g:link controller="content" action="create">create</g:link>
          </g:else>
              </td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name">Content:</td>

            <td  valign="top" style="text-align:left;" class="value">
              <ul>
                <g:each var="c" in="${itemInstance.content.sort{ def a, def b -> b.id <=> a.id }}">
                  <li><g:link controller="content" action="show" id="${c.id}">
                    ${c.id}&nbsp;
                    &quot;${c?.title.encodeAsHTML()}&quot;
                    <g:if test="${c.lastUpdated}">edited <prettytime:display date="${c?.lastUpdated}"/></g:if>
                    by ${c?.author?.username.encodeAsHTML()}</g:link></li>
                </g:each>
              </ul>
            </td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name">Tags:</td>

            <td  valign="top" style="text-align:left;" class="value">
              <ul>
                <g:each var="t" in="${itemInstance.tags}">
                  <li><g:link controller="tag" action="show" id="${t.id}">${t?.name.encodeAsHTML()}</g:link></li>
                </g:each>
              </ul>
            </td>

          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:form>
          <input type="hidden" name="id" value="${itemInstance?.id}" />
          <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
          <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
        </g:form>
      </div>
    </div>
  </body>
</html>
