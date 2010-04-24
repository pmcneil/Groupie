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
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="dialog sponsor">
  <g:render template="trail" model="['step': '3']"/>

  <g:if test="${message}">
    <div class="message">${message}</div>
  </g:if>

  <h1>Confirm</h1>
  <p>Your details are:-</p>

${uc.username}<br/>
${uc.salutation} ${uc.firstName} ${uc.lastName}<br/>
${uc.email}<br/>
${mc.address}<br/>
${mc.state}<br/>
${mc.postcode}<br/>
${mc.phoneType} phone - ${mc.contactPhone} <g:if test="${mc.phoneNote}">(${mc.phoneNote})</g:if>

  <g:form action="join" onsubmit="return !Groupie.isAjaxSubmit;">
    <div class="buttons">
      <input type="submit" id="_eventId_back" value="Back" name="_eventId_back" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input type="submit" id="_eventId_next" value="Confirm" name="_eventId_next" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input id="_eventId_cancel" type="submit" value="Exit" name="_eventId_cancel" onclick="Groupie.isAjaxSubmit = false;"/>
    </div>
  </g:form>
</div>