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
  template for login/out tag
-->

<%@ page contentType="text/html;charset=UTF-8" %>
<ul class="menu login">
  <g:isLoggedIn>
    <li>
      <a href="${g.createLink(controller: 'logout')}" title="Logged in as ${g.loggedInUserInfo(field: "username")}">
        logout
      </a>
      <ul>
        <li><a href="${g.createLink(controller: 'register', action: 'edit')}"
               class="boxy"
               title="Edit your profile.">
            My details
          </a>
        </li>
      </ul>
    </li>
  </g:isLoggedIn>
  <g:isNotLoggedIn>
    <li>
      <a href="${g.createLink(controller: 'login', action: 'ajaxauth')}"
         class="boxy"
         title="Login">login</a>
      <ul>
        <li><a href="${g.createLink(controller: 'register', action: 'reg')}"
               class="boxy"
               title="Register.">
            register
          </a>
        </li>
      </ul>
    </li>
  </g:isNotLoggedIn>
</ul>
