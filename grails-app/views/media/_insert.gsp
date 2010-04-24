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

<%@ page import="com.nerderg.groupie.content.Media" %>
<div>
  <div id="insertMedia" style="overflow: auto; width: 710px;">
    <h1>Media List</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="tabbox">
      <div class="tabs">
        <ul >
          <li <g:if test="${selectedType == 'image'}">class="selected"</g:if> onclick="EIP.showMedia(this, 'image');">images</li>
          <li <g:if test="${selectedType == 'audio'}">class="selected"</g:if> onclick="EIP.showMedia(this, 'audio');">audio</li>
          <li <g:if test="${selectedType == 'video'}">class="selected"</g:if> onclick="EIP.showMedia(this, 'video');">video</li>
          <li <g:if test="${selectedType == 'document'}">class="selected"</g:if> onclick="EIP.showMedia(this, 'document');">docs</li>
          <li <g:if test="${!selectedType}">class="selected"</g:if> onclick="EIP.showMedia(this, 'other');">all/other</li>
        </ul>
      </div>
      <div id="mediaList" class="list">
        <g:each in="${mediaInstanceList}" status="i" var="mediaInstance">

          <g:if test="${mediaInstance.mimeType.startsWith('image')}">
            <div class="media mediaPreview image">
                <img src="${g.createLink(controller: 'media', action:'show', id: mediaInstance.id, params: [w: 100, h:100, a: on])}" title="file: ${mediaInstance.filename}" alt="${mediaInstance.filename}"/>
                <a href="media/delete/${mediaInstance.id}?type=${selectedType}"
                   title="Delete Image"
                   style="position: absolute; top: 95px; left: 95px;"
                   onclick="return Groupie.ajaxPage(this);">
                  <img src="${resource(dir:'images/icons/silk',file:'delete.png')}" alt="Spinner" />
                </a>
            </div>
          </g:if>
          <g:else>

            <div id="media-${mediaInstance.id}" class="media ${selectedType}" >
              <a class="${selectedType}"
                 href="${g.createLink(controller: 'media', action:'show', id: mediaInstance.id)}"
                 draggable="true"
                 ondragstart="event.dataTransfer.clearData(); event.dataTransfer.setData('text/plain','media-${mediaInstance.id}');"
                 title="drag to required location">
${mediaInstance.filename}
              </a>
              <a href="media/delete/${mediaInstance.id}?type=${selectedType}" title="Delete Image" style="float: right;" onclick="return Groupie.ajaxPage(this);">
                <img src="${resource(dir:'images/icons/silk',file:'delete.png')}" alt="Spinner" />
              </a>
            </div>
          </g:else>
        </g:each>
      </div>

    </div>
    <div class="paginateButtons">
      <span>
        <a class="upload" href="${g.createLink(controller: 'media', action: 'save')}">
          Upload
        </a>
      </span>
      <g:paginate total="${mediaInstanceTotal}" params="['type':selectedType]"/>
    </div>
  </div>
</div>