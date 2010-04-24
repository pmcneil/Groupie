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
<!-- needs
editorName e.g. menu,
items a list of possible things to edit,
type html/css
contentInst the Content to edit
-->
<div id="${editorName}Editor">

  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>

    <g:form controller="${editorName}" action="edit" method="post" style="display: inline-block">

      <g:select optionKey="id"
                optionValue="name"
                from="${items}"
                name="id"
                value=""
                noSelection="${['null':'Select a ' + editorName]}"
                onchange="return Groupie.postForm(this.form, function(ret){
                    var ed = \$('#${editorName}Editor');
                    ed.html(ret);
                  });">

      </g:select> or
    </g:form>
    <g:form  controller="${editorName}" action="create" method="post" style="display: inline-block">
      <span class="button">
        <input class="create" type="button" value="New ${editorName}" onclick="return Groupie.postForm(this.form, function(ret){
                    var ed = $('#${editorName}Editor');
                    ed.html(ret);
                  });">
      </span>
    </g:form>
  <hr/>
  <g:if test="${contentInst != null}">
    <g:hasErrors bean="${contentInst}">
      <div class="errors">
        <g:renderErrors bean="${contentInst}" as="list" />
      </div>
    </g:hasErrors>

    <g:form  controller="${editorName}" action="${action}" method="post">
      <div class="dialog">
        <input type="hidden" name="id" value="${contentInst.id}">

        <nerderg:inputfield label="Name" field="name" bean="${contentInst.item}"/>

        <nerderg:inputfield label="Title" field="title" bean="${contentInst}"/>

        <nerderg:formfield label="Content"/>
        <div>
          <g:textArea cols="80" rows="20" name="content" value="${contentInst.content}"/>
        </div>
      </div>
      <div class="buttons">
        <span class="button">
          <input class="save" type="button" value="${action}"
                 onclick="this.form.content.value = editAreaLoader.getValue('content');
               for(var i in editAreas){
                 editAreaLoader.delete_instance(i);
               }
             return Groupie.postForm(this.form, function(ret){$('#${editorName}Editor').html(ret);});"/>
        </span>
      </div>
    </g:form>
    <g:form  controller="${editorName}" action="revert" method="post">
      <div class="dialog">
        <hr/>
        <input type="hidden" name="id" value="${contentInst.id}">

        <nerderg:formfield label="Revert To">
          <g:select optionKey="id"
                    from="${contentInst.item.content?.sort{ def a, def b -> b.id <=> a.id }}"
                    optionValue='${{it.id + ":" + it.title.encodeAsHTML() + " edited " + prettytime.display(date: it.lastUpdated) + " by " + it.author.username.encodeAsHTML()}}'
                    name="currentId"
                    value="${contentInst.item?.current?.id}"
                    noSelection="['null':'']"
                    onChange="return Groupie.postForm(this.form, function(ret){\$('#${editorName}Editor').html(ret);});"></g:select>
        </nerderg:formfield>
        <hr/>
      </div>
    </g:form>

  </g:if>
</div>
