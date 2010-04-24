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
display a form to upload media files
-->

<%@ page contentType="text/html;charset=UTF-8" %>
<div id="mediaUpload">
  <g:uploadForm action="save">
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${media}">
      <h2>Media Problems</h2>
      <div class="errors">
        <g:renderErrors bean="${media}" as="list" />
      </div>
    </g:hasErrors>

    <g:hasErrors bean="${content}">
      <h2>Content Problems</h2>
      <div class="errors">
        <g:renderErrors bean="${content}" as="list" />
      </div>
    </g:hasErrors>

    <nerderg:inputfield label="Description" bean="${media}" field="description"/>

    <nerderg:formfield label="Media" bean="${media}" field="filename">
      <input name="media" type="file" value="${media?.filename}"/>
    </nerderg:formfield>
    <div class="buttons">
      <span class="button">
        <input class="save" type="submit" value="Upload"/>
      </span>
    </div>
  </g:uploadForm>
</div>
