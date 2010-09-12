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
<%@ page import="com.nerderg.groupie.member.Phone" %>
<util:logit message="phoneInst: ${inst}"/>
<nerderg:formfield label="Type" field="type" bean="${inst}">
  <g:select from="${com.nerderg.groupie.member.Phone.types}" name="type" value="${inst?.type}" ></g:select>
</nerderg:formfield>

<nerderg:inputfield label="Number" field="number" bean="${inst}"/>

<nerderg:inputfield label="Note" field="note" bean="${inst}"/>

<nerderg:formfield label="Preferred Number?" field="preferred" bean="${inst}">
  <g:checkBox name="preferred" value="${inst?.preferred}" />
</nerderg:formfield>
