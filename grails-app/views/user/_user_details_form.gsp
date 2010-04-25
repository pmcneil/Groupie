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
<nerderg:inputfield label="Login Name"  bean="${person}" field="username"/>

<nerderg:formfield label="Salutation"  bean="${person}" field="salutation">
  <g:select name="salpick" from="${['Mr','Mrs','Ms']}" noSelection="${['other':'other']}"
            value="${person?.salutation}"
            onchange="this.form.salutation.value = (this.value == 'other' ? '' : this.value);"></g:select>
  <input type="text" size="10" id="salutation" name="salutation" value="${person?.salutation}"/>
</nerderg:formfield>

<nerderg:inputfield label="First Name" bean="${person}" field="firstName"/>

<nerderg:inputfield label="Last Name" bean="${person}" field="lastName"/>

<nerderg:formfield label="Password" bean="${person}" field="passwd">
  <input type="password" name='passwd' value=""/>
</nerderg:formfield>

<nerderg:formfield label="Enabled" bean="${person}" field="enabled">
  <g:checkBox name="enabled" value="${person.enabled}" ></g:checkBox>
</nerderg:formfield>

<nerderg:inputfield label="Email" field="email" bean="${person}"/>

<nerderg:inputfield label="Description" field="description" bean="${person}"/>

<nerderg:formfield label="Show email" bean="${person}" field="emailShow">
  <g:checkBox name="emailShow" value="${person.emailShow}"/>
</nerderg:formfield>

<h2>Roles:</h2>

<g:each var="entry" in="${roleMap}">
  <nerderg:formfield label="${entry.key.authority}" bean="${person}" field="authorities">
    <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
  </nerderg:formfield>
</g:each>

<h2>Mailing Lists:</h2>

<g:each var="mlist" in="${com.nerderg.groupie.mail.MailingList.list()}">
  <nerderg:formfield label="${mlist.name}">
    <g:checkBox name="mlist-${mlist.id}" value="${mlist.users.contains(person)}"/>
  </nerderg:formfield>
</g:each>
