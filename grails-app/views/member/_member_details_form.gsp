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
<%@ page import="com.nerderg.groupie.member.Address" %>
<%@ page import="com.nerderg.groupie.member.MemberData" %>
<%@ page import="com.nerderg.groupie.member.MemberType" %>

<div id="memberDetailMsg"></div>
<div id="memberDetailErrors"></div>

<nerderg:inputfield label="Date joined" field="date" bean="${memberInstance}"
                    onchange="Groupie.postData('${createLink(controller: 'member', action: 'ajaxUpdateField')}', 'id=${memberInstance.id}&date='+this.value,
                         function(d, opts){Groupie.status(d, opts);}, false, {el: this})">
</nerderg:inputfield>

<nerderg:inputfield label="Expiry Date" field="expiryDate" bean="${memberInstance}"
                    onchange="Groupie.postData('${createLink(controller: 'member', action: 'ajaxUpdateField')}', 'id=${memberInstance.id}&expiryDate='+this.value,
                         function(d, opts){Groupie.status(d, opts);}, false, {el: this})">
</nerderg:inputfield>

<groupie:linkedFieldEditor label="Member Address" field="memberAddress"
                        inst="${memberInstance}" instName="Member"
                        controller="address" reload="reloadDetails('${memberInstance.user.id}');"/>

<groupie:linkedFieldEditor label="Phone Numbers" field="memberPhone"
                        inst="${memberInstance}" instName="Member"
                        controller="phone" reload="reloadDetails('${memberInstance.user.id}');"/>

<groupie:linkedFieldEditor label="Member Data" field="memberData"
                        inst="${memberInstance}" instName="Member"
                        controller="memberData" reload="reloadDetails('${memberInstance.user.id}');"/>

<a class="field_editor" href="${createLink(controller: 'dataTag', action: 'list')}">edit data tags</a>

<nerderg:checkboxgroup label="Member Flags" field="memberFlags" bean="${memberInstance}"
                       from="${com.nerderg.groupie.member.MemberFlags.list()}"
                       subset="${memberInstance.memberFlags}"
                       onchange="Groupie.postData('${createLink(controller: 'member', action: 'ajaxUpdateField')}', 'id=${memberInstance.id}&'+jQuery(this.form).serialize(),
                         function(d, opts){Groupie.status(d, opts);}, false, {el: this})"/>

<a class="field_editor" href="${createLink(controller: 'memberFlags', action: 'list')}">edit flags</a>

<nerderg:formfield label="Member Type" field="memberType" bean="${memberInstance}">
  <g:select optionKey="id" from="${com.nerderg.groupie.member.MemberType.list()}" name="memberType.id" value="${memberInstance?.memberType?.id}" 
            onchange="Groupie.postData('${createLink(controller: 'member', action: 'ajaxUpdateField')}', 'id=${memberInstance?.id}&memberType='+this.value,
              function(d, opts){Groupie.status(d, opts);}, false, {el: this})">
  </g:select>
  <a class="field_editor" href="${createLink(controller: 'memberType', action: 'list')}">edit types</a>
</nerderg:formfield>
