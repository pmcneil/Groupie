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
<%@ page import="com.nerderg.groupie.donate.Sponsorship" %>

<div class="list">
  <table>
    <thead>
      <tr>

    <g:sortableColumn property="id" title="Id" />

    <g:sortableColumn property="startDate" title="Start Date" />

    <g:sortableColumn property="endDate" title="End Date" />

    <g:sortableColumn property="frequency" title="Frequency" />

    <th>Target</th>

    </tr>
    </thead>
    <tbody>
    <g:each in="${Sponsorship.findAllByMember(memberInstance)}" status="i" var="sponsorshipInstance">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

        <td><g:link controller="sponsorship" action="show" id="${sponsorshipInstance.id}">${fieldValue(bean:sponsorshipInstance, field:'id')}</g:link></td>

      <td>${fieldValue(bean:sponsorshipInstance, field:'startDate')}</td>

      <td>${fieldValue(bean:sponsorshipInstance, field:'endDate')}</td>

      <td>${fieldValue(bean:sponsorshipInstance, field:'frequency')}</td>

      <td>
        <g:link controller="target" action="show" id="${sponsorshipInstance.target.id}">${sponsorshipInstance.target}</g:link>
    </td>

      </tr>
    </g:each>
    </tbody>
  </table>
</div>
