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
<g:if test="${mediaInstance.mimeType.split('/')[1] == 'ogg' && type =='audio'}">
  <${type} src="${g.createLink(controller: 'media', action:'show', id: mediaInstance.id)}" controls="true">
</g:if>
<a class="media ${type} ${mediaInstance.mimeType.split('/')[1]}"
   href="${g.createLink(controller: 'media', action:'show', id: mediaInstance.id, params: [file: mediaInstance.filename])}"
   draggable="false"
   title="${mediaInstance.filename}">
${mediaInstance.filename}
</a>

<g:if test="${mediaInstance.mimeType.split('/')[1] == 'ogg' && type =='audio'}">
  </${type}>
</g:if>
