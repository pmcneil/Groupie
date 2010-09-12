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
  
  <util:logit message="rendering site registration (member)"/>

  <h1>Site registration details</h1>

  <h2>login</h2>

  <g:form action="join" onsubmit="return !Groupie.isAjaxSubmit;">

    <nerderg:formfield label="Login ID" errors="false">
      <input type='text' name='j_username' id='j_username' />
    </nerderg:formfield>
    <nerderg:formfield label="Password" errors="false">
      <input type='password' class='text_' name='j_password' id='j_password' />
    </nerderg:formfield>
    <nerderg:formfield label="Remember me" errors="false">
      <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'
    </nerderg:formfield>
    <span class="button">
      <input type="submit" id="_eventId_login" value="Login" name="_eventId_login" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
    </span>
  </g:form>

  <h2>or sign-up</h2>

  <g:form action="join" onsubmit="return !Groupie.isAjaxSubmit;">

    <div class="dialog">
      <g:hasErrors bean="${uc}">
        <div class="errors">
          <g:renderErrors bean="${uc}"/>
        </div>
      </g:hasErrors>

      <nerderg:inputfield label="Login Name"  bean="${uc}" field="username"/>

      <nerderg:formfield label="Salutation"  bean="${uc}" field="salutation">
        <g:select name="salpick" from="${['Mr','Mrs','Ms']}" noSelection="${['other':'other']}"
                  value="${uc?.salutation}"
                  onchange="this.form.salutation.value = (this.value == 'other' ? '' : this.value);"></g:select>
        <input type="text" size="10" id="salutation" name="salutation" value="${uc?.salutation}"/>
      </nerderg:formfield>

      <nerderg:inputfield label="First Name" bean="${uc}" field="firstName"/>

      <nerderg:inputfield label="Last Name" bean="${uc}" field="lastName"/>

      <nerderg:formfield label="Password" bean="${uc}" field="passwd">
        <input type="password" name='passwd' value=""/>&nbsp;<span class="note">(make it hard to guess)</span>
      </nerderg:formfield>

      <nerderg:formfield label="Confirm Password" bean="${uc}" field="passwd">
        <input type="password" name='repasswd' value=""/>
      </nerderg:formfield>

      <nerderg:inputfield label="Email" field="email" bean="${uc}"/>

      <nerderg:inputfield label="Enter Code" field="captcha" size="8">
        <img src="${createLink(controller:'captcha', action:'index')}" align="absmiddle"/>
      </nerderg:inputfield>

    </div>

    <div class="buttons">
      <input type="submit" id="_eventId_back" value="Back" name="_eventId_back" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input type="submit" id="_eventId_next" value="Next" name="_eventId_next" onclick="Groupie.ajaxSubmitAndReplace(this)"/>
      <input id="_eventId_cancel" type="submit" value="Exit" name="_eventId_cancel" onclick="Groupie.isAjaxSubmit = false;"/>
    </div>
  </g:form>
</div>
