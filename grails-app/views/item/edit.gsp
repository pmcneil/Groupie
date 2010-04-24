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
    <title>Edit Item</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="list" action="list">Item List</g:link></span>
      <span class="menuButton"><g:link class="create" action="create">New Item</g:link></span>
    </div>
    <div class="body">
      <h1>Edit Item</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${itemInstance}">
        <div class="errors">
          <g:renderErrors bean="${itemInstance}" as="list" />
        </div>
      </g:hasErrors>
      <g:form method="post" >
        <input type="hidden" name="id" value="${itemInstance?.id}" />
        <input type="hidden" name="version" value="${itemInstance?.version}" />
        <div class="dialog">
          <table>
            <tbody>

              <tr class="prop">
                <td valign="top" class="name">
                  <label for="type">Type:</label>
                </td>
                <td valign="top" class="value ${hasErrors(bean:itemInstance,field:'type','errors')}">
            <g:select id="type" name="type" from="${itemInstance.constraints.type.inList}" value="${itemInstance.type}" ></g:select>
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="current">Current:</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean:itemInstance,field:'current','errors')}">
            <g:select optionKey="id"
                      from="${itemInstance.content.sort{ def a, def b -> b.id <=> a.id }}"
                      optionValue='${{it.id + ":" + it.title.encodeAsHTML() + " edited " + prettytime.display(date: it.lastUpdated) + " by " + it.author.username.encodeAsHTML()}}'
                      name="current.id"
                      value="${itemInstance?.current?.id}"
                      noSelection="['null':'']"></g:select>
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="content">Content:</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean:itemInstance,field:'content','errors')}">

                <ul>
                  <g:each var="c" in="${itemInstance.content.sort{ def a, def b -> b.id <=> a.id }}">
                    <li><g:link controller="content" action="show" id="${c.id}">
${c.id}&nbsp;
                      &quot;${c?.title.encodeAsHTML()}&quot;
                      <g:if test="${c.lastUpdated}">edited <prettytime:display date="${c?.lastUpdated}"/></g:if>
                      by ${c?.author?.username.encodeAsHTML()}</g:link></li>
                  </g:each>
                </ul>
            <g:link controller="content" params="['item.id':itemInstance?.id]" action="create">Add Content</g:link>

            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="tags">Tags:</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean:itemInstance,field:'tags','errors')}">
            <g:select name="tags"
                      from="${com.nerderg.groupie.content.Tag.list()}"
                      size="5" multiple="yes" optionKey="id"
                      value="${itemInstance?.tags}" />

            </td>
            </tr>

            </tbody>
          </table>
        </div>
        <div class="buttons">
          <span class="button"><g:actionSubmit class="save" value="Update" /></span>
          <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
