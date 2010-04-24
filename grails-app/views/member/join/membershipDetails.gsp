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
<%@ page import="com.nerderg.groupie.member.Address" %>
<%@ page import="com.nerderg.groupie.member.Phone" %>
<div class="dialog sponsor">
  <g:render template="trail" model="['step': '2']"/>

  <g:if test="${message}">
    <div class="message">${message}</div>
  </g:if>

  <h1>New member details</h1>

  <g:hasErrors bean="${mc}">
    <div class="errors">
      <g:renderErrors bean="${mc}"/>
    </div>
  </g:hasErrors>

  <g:form action="join" onsubmit="return !Groupie.isAjaxSubmit;">
    <nerderg:formfield label="Address" field="address" bean="${mc}">
      <textarea id="address" name="address" cols="30" rows="2" value="${fieldValue(bean:mc,field:'address')}">${fieldValue(bean:mc,field:'address')}</textarea>
    </nerderg:formfield>

    <nerderg:inputfield label="Postcode/Zip" field="postcode" bean="${mc}" size="6"/>

    <nerderg:formfield label="State" field="state" bean="${mc}">
      <g:select id="state" name="state" from="${Address.states}" value="${mc?.state}" ></g:select>
    </nerderg:formfield>

    <nerderg:formfield label="Country" field="country" bean="${mc}">
      <g:select id="country" name="country" from="${Address.countries}" value="${mc?.country}" ></g:select>
    </nerderg:formfield>

    <nerderg:formfield label="Contact Phone" field="contactPhone" bean="${mc}">
      <g:select id="phoneType" name="phoneType" from="${Phone.types}" value="${mc?.phoneType}"></g:select>
      <input type="text" id="contactPhone" name="contactPhone" value="${mc?.contactPhone}"/>
      (Notes? <input type="text" id="phoneNote" name="phoneNote" value="${mc?.phoneNote}"/>)
    </nerderg:formfield>

    <div class="buttons">
      <input type="submit" id="_eventId_back" value="Back" name="_eventId_back" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input type="submit" id="_eventId_next" value="Next" name="_eventId_next" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input id="_eventId_cancel" type="submit" value="Exit" name="_eventId_cancel" onclick="Groupie.isAjaxSubmit = false;"/>
    </div>
  </g:form>
</div>