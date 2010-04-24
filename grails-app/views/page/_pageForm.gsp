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
<%@ page contentType="text/html;charset=UTF-8" %>

<nerderg:inputfield label="Name" field="name" bean="${pageInst.item}"/>

<nerderg:inputfield label="Tile" field="title" bean="${pageInst}"/>

<nerderg:formfield label="Parent" field="parent" bean="${pageInst.item}">
  <g:select optionKey="id"
            optionValue="name"
            from="${com.nerderg.groupie.content.Item.findAllByTypeOrType('page','menu')}"
            name="parent"
            value="${pageInst.item.parent?.id}"
            noSelection="${['null':'No parent']}"></g:select>
</nerderg:formfield>

<nerderg:formfield label="Order" field="pageOrder" bean="${pageInst.item.params}">
  <g:select from="${0..100}" name="pageOrder" value="${pageInst.item.params.pageOrder}" ></g:select>
</nerderg:formfield>

<nerderg:formfield label="Read Access" field="readPerms" bean="${pageInst.item}">
  <g:select
    from="${pageInst.item.constraints.readPerms.inList}"
    name="readPerms"
    value="${pageInst.item.readPerms}">
  </g:select>
</nerderg:formfield>

<nerderg:formfield label="Write Access" field="writePerms" bean="${pageInst.item}">
  <g:select
    from="${pageInst.item.constraints.writePerms.inList}"
    name="writePerms"
    value="${pageInst.item.writePerms}">
  </g:select> <g:if test="${pageInst.author}">(Author: ${pageInst.author?.username?.encodeAsHTML()})</g:if>
</nerderg:formfield>

<nerderg:formfield label="Layout" field="layout">
  <g:select optionKey="id"
            optionValue="name"
            from="${com.nerderg.groupie.content.Layout.list()}"
            name="layout"
            value="${pageInst.item.params.layoutId}" >
  </g:select>
</nerderg:formfield>

<div class="toggle">
  <a id="showOptions" class="optionsClosed" href="#"></a>
</div>
<div class="options">

  <nerderg:formfield label="Template" field="template">
    <g:select optionKey="id"
              optionValue="name"
              from="${com.nerderg.groupie.content.Item.findAllByType('template')}"
              name="template"
              value="${pageInst.item.params.template}"
              onchange="${remoteFunction(controller: 'page',
action: 'changeTemplate',
update:[success:'content', failure: 'console'],
params: '\'id=\' + this.value',
after: 'Groupie.updateValue(\'content\')'
)}"></g:select>
  </nerderg:formfield>

  <nerderg:formfield label="Content" field="content" bean="${pageInst}">
    <g:textArea cols="100" rows="20" name="content" value="${pageInst.content}"/>
  </nerderg:formfield>

</div>
