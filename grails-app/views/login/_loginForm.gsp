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
<!--
Login form
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<g:if test='${flash.message}'>
  <div class='login_message'>${flash.message}</div>
</g:if>
<form action='${postUrl}' method='POST' id='loginForm' class='cssform'>

  <input type="hidden" name="spring-security-redirect" value="${redirectUrl}"/>
  <nerderg:formfield label="Login ID" >
    <input type='text' name='j_username' id='j_username' value='${request.remoteUser}' />
  </nerderg:formfield>

  <nerderg:formfield label="Password">
    <input type='password' class='text_' name='j_password' id='j_password' />
  </nerderg:formfield>

  <nerderg:formfield label="Remember me">
    <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'
           <g:if test='${hasCookie}'>checked='checked'</g:if> />
  </nerderg:formfield>
  <span class="button">
    <input type='submit' value='Login' />
  </span>
</form>
