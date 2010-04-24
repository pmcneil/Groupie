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
  <meta name='layout' content='main' />
  <title>Login</title>
</head>

<body>
<g:if test='${flash.message}'>
  <div class='login_message'>${flash.message}</div>
</g:if>
<div style="margin: auto; width: 42em;">
  <h2>Please login</h2>
  <div class="dialog">

    <form action='${postUrl}' method='POST' id='loginForm' class='cssform'>

      <nerderg:formfield label="Login ID" errors="false">
        <input type='text' name='j_username' id='j_username' />
      </nerderg:formfield>
      <nerderg:formfield label="Password" errors="false">
        <input type='password' class='text_' name='j_password' id='j_password' />
      </nerderg:formfield>
      <nerderg:formfield label="Remember me" errors="false">
        <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'
      </nerderg:formfield>
      <span class="name">
        press -->
      </span>
      <span class="value">
        <input type='submit' value='Login' />
      </span>
    </form>
  </div>
  <h2 class="toggleNext">or sign-up</h2>
  <div class="dialog">
    <g:form controller="register" action="save">
      <input type="hidden" name="redirectTo" value="${session.SPRING_SECURITY_SAVED_REQUEST_KEY?.getFullRequestUrl()}"/>

      <g:render template="/register/details_form" bean="${person}"/>

      <nerderg:inputfield label="Enter Code" field="captcha" size="8">
        <img src="${createLink(controller:'captcha', action:'index')}" alt="captcha should be here" align="absmiddle"/>
      </nerderg:inputfield>

      <span class="name">
        press -->
      </span>
      <span class="value">
        <input class='save' type="submit" value="Signup"/>
      </span>

    </g:form>
  </div>
</div>
<script type='text/javascript'>
<!--
(function(){
        document.forms['loginForm'].elements['j_username'].focus();
})();
// -->
</script>
</body>
