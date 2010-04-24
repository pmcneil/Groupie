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
  <g:render template="trail" model="['step': '1']"/>

  <g:if test="${message}">
    <div class="message">${message}</div>
  </g:if>

  <h1>Select Child</h1>

  <g:form action="child" onsubmit="return !Groupie.isAjaxSubmit;">
    <nerderg:formfield label="Pick for me">
      <g:radio id="_eventId_pick" value="Pick for me" name="_eventId_pick" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
    </nerderg:formfield>
  </g:form>

  <g:form action="child" onsubmit="return !Groupie.isAjaxSubmit;">
    <g:each var="child" in="${children}">
      <groupie:show name="${child.name}"/>
      <nerderg:formfield label="Sponsor Me" bean="${child}" field="id">
        <g:radio name="id" value="${child.id}" checked="${chosenone?.id == child.id}"/>
      </nerderg:formfield>
    </g:each>
    <div class="buttons">
      <input type="submit" id="_eventId_back" value="Back" name="_eventId_back" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input type="submit" id="_eventId_next" value="Next" name="_eventId_next" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input type="submit" id="_eventId_cancel" value="Exit" name="_eventId_cancel" onclick="Groupie.isAjaxSubmit = false;"/>
    </div>
  </g:form>

  <nerderg:cleanflowscope flow="${this}"/>
</div>