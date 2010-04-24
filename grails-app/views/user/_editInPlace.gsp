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

<div>
  <h1>${user.firstName} ${user.lastName} (${user.username}) <g:if test="${user.member}"><span class="member">member</span></g:if></h1>
  <div class="boxeddialog">
    <h2 class="toggleNext">User details</h2>
    <div id="user_deets">

      <g:form controller="user" action="ajaxUpdate" onsubmit="return false;">
        <input type="hidden" name="id" value="${user.id}" />
        <input type="hidden" name="version" value="${user.version}" />
        <groupie:userEditor user="${user}"/>
        <div class="buttons">
          <span class="button">
            <g:actionSubmit class="save ajaxForm" value="Update"
                            onclick="Groupie.postData(this.form.action, jQuery(this.form).serialize(),
                              function(d, opts) {
                                 Groupie.status(d, opts);
                                 if(!d.errors) {
                                   reloadDetails('${user.id}');
                                  }
                              }, false, {el: this});"/>
          </span>
        </div>
      </g:form>
    </div>
  </div>

  <g:if test="${user.member}">
    <div class="boxeddialog">
      <h2 class="toggleNext">Membership details</h2>
      <div id="membership_deets">
        <g:render template="/member/member_details_form" model="['memberInstance': user.member]"/>
      </div>
    </div>

    <div class="boxeddialog">
      <h2 class="toggleNext">Sponsorship details</h2>
      <div id="sponsorship_deets">
        <g:render template="/sponsorship/member_sponsorships" model="['memberInstance': user.member]"/>
      </div>

    </div>
  </g:if>
  <g:else>
    <div  class="boxeddialog">
      <a href="${createLink(controller: 'user', action: 'makeUserMember', id: user.id)}">make member</a>
    </div>
  </g:else>

</div>