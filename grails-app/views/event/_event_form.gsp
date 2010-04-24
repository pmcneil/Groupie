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
<div class="dialog">
  <nerderg:inputfield label="Name" bean="${eventInstance}" field="name" size="42"/>
  <nerderg:inputfield label="Location" bean="${eventInstance}" field="location" size="42"/>

  <nerderg:formfield label="Description" bean="${eventInstance}" field="description">
    <g:textArea name="description" cols="40" rows="5" value="${eventInstance?.description}" />
  </nerderg:formfield>

  <nerderg:datetimefield label="Start time" field="startDate" bean="${eventInstance}" class="date" size="10"/>

  <nerderg:datetimefield label="End time" field="endDate" bean="${eventInstance}" class="date" size="10"/>

  <nerderg:formfield label="Organiser" bean="${eventInstance}" field="organiser">
    <g:select name="organiser.id" from="${com.nerderg.groupie.member.Member.list()}" optionKey="id" value="${eventInstance?.organiser?.id}"  />
  </nerderg:formfield>

  <nerderg:formfield label="RSVPs" bean="${eventInstance}" field="rsvp">
    <g:link controller="rsvp" action="create" params="['event.id': eventInstance?.id]">
      [RSVP]
    </g:link>

    <ul>
      <g:each in="${eventInstance?.rsvp?}" var="r">
        <li><g:link controller="rsvp" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
      </g:each>
    </ul>
  </nerderg:formfield>

</div>
