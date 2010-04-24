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

<%@ page import="com.nerderg.groupie.content.Media" %>
<html>
  <head>
    <meta http-equiv="media-Type" media="text/html; charset=UTF-8"/>
    <meta name="layout" media="main" />
    <title>Media List</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
      <span class="menuButton"><g:link class="create" action="create">New media</g:link></span>
    </div>
    <div class="body">
      <h1>Media List</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="list">
        <table>
          <thead>
            <tr>

          <g:sortableColumn property="id" title="Id" />

          <g:sortableColumn property="filename" title="filename" />

          <g:sortableColumn property="description" title="Description" />

          <th>Data</th>

          </tr>
          </thead>
          <tbody>
          <g:each in="${mediaInstanceList}" status="i" var="mediaInstance">
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

              <td><g:link action="show" id="${mediaInstance.id}">${fieldValue(bean:mediaInstance, field:'id')}</g:link></td>

            <td>${fieldValue(bean:mediaInstance, field:'filename')}</td>

            <td>${fieldValue(bean:mediaInstance, field:'description')}</td>

            <td><img src="<g:createLink controller='media' action='show' id='${mediaInstance.id}'/>"/></td>

            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div class="paginateButtons">
        <g:paginate total="${mediaInstanceTotal}" />
      </div>
    </div>
  </body>
</html>
