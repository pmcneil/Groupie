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
	<title>Requestmap List</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
		<span class="menuButton"><g:link class="create" action="create">New Requestmap</g:link></span>
	</div>

	<div class="body">
		<h1>Requestmap List</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="list">
			<table>
			<thead>
				<tr>
					<g:sortableColumn property="id" title="ID" />
					<g:sortableColumn property="url" title="URL Pattern" />
					<g:sortableColumn property="configAttribute" title="Roles" />
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			<g:each in="${requestmapList}" status="i" var="requestmap">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${requestmap.id}</td>
					<td>${requestmap.url?.encodeAsHTML()}</td>
					<td>${requestmap.configAttribute}</td>
					<td class="actionButtons">
						<span class="actionButton">
						<g:link action="show" id="${requestmap.id}">Show</g:link>
						</span>
					</td>
				</tr>
				</g:each>
			</tbody>
			</table>
		</div>

		<div class="paginateButtons">
			<g:paginate total="${com.nerderg.groupie.Requestmap.count()}" />
		</div>

	</div>
</body>
