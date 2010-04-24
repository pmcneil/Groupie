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
<nerderg:inputfield label="Address" field="address" bean="${inst}"/>

<nerderg:inputfield label="Postcode" field="postcode" bean="${inst}"/>

<nerderg:formfield label="State" field="state" bean="${inst}">
  <g:select id="state" name="state" from="${com.nerderg.groupie.member.Address.states}" value="${inst?.state}" ></g:select>
</nerderg:formfield>

<nerderg:formfield label="Country" field="country" bean="${inst}">
  <g:select id="country" name="country" from="${com.nerderg.groupie.member.Address.countries}" value="${inst?.country}" ></g:select>
</nerderg:formfield>
