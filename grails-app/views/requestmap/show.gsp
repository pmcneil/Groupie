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
	<title>Show Requestmap</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
		<span class="menuButton"><g:link class="list" action="list">Requestmap List</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">New Requestmap</g:link></span>
	</div>

	<div class="body">
		<h1>Show Requestmap</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="dialog">
			<table>
			<tbody>

				<tr class="prop">
					<td valign="top" class="name">ID:</td>
					<td valign="top" class="value">${requestmap.id}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">URL:</td>
					<td valign="top" class="value">${requestmap.url}</td>
				</tr>

				<tr class="prop">
					<td valign="top" class="name">Roles:</td>
					<td valign="top" class="value">${requestmap.configAttribute}</td>
				</tr>

			</tbody>
			</table>
		</div>

		<div class="buttons">
			<g:form>
				<input type="hidden" name="id" value="${requestmap.id}" />
				<span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
				<span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
			</g:form>
		</div>

	</div>
</body>
