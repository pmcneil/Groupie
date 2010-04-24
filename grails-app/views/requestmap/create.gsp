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
	<title>Create Requestmap</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
		<span class="menuButton"><g:link class="list" action="list">Requestmap List</g:link></span>
	</div>

	<div class="body">
		<h1>Create Requestmap</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${requestmap}">
		<div class="errors">
			<g:renderErrors bean="${requestmap}" as="list" />
		</div>
		</g:hasErrors>
		<g:form action="save">
			<div class="dialog">
				<table>
				<tbody>

					<tr class="prop">
						<td valign="top" class="name"><label for="url">URL Pattern:</label></td>
						<td valign="top" class="value ${hasErrors(bean:requestmap,field:'url','errors')}">
							<input type="text" id="url" name="url" value="${requestmap.url?.encodeAsHTML()}"/>
						</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name"><label for="configAttribute">Role (comma-delimited):</label></td>
						<td valign="top" class="value ${hasErrors(bean:requestmap,field:'configAttribute','errors')}">
							<input type="text" id="configAttribute" name="configAttribute" value="${requestmap.configAttribute?.encodeAsHTML()}"/>
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
