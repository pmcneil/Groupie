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
<div id="stylist" style="display:none;">
  <p>Click target to edit surrounding tag</p>
  <form id="stylist_form" action="">
    <nerderg:inputfield label="Tag" field="target-tag">
      <a href="#" id="stylist_parent" title="Parent">
        <img src="${resource(dir:'images/icons/silk',file:'arrow_up.png')}" alt="up"/>
      </a>
    </nerderg:inputfield>
    <nerderg:inputfield label="Id" field="target-id"/>
    <nerderg:inputfield label="Classes" field="target-classes" class="classEntry">
      <a href="${g.createLink(controller: 'style', action: 'editPageStyle', params: ['name': 'default'])}"
         class="boxy"
         title="Edit default style">
        <img src="${resource(dir:'images/icons/silk',file:'css_go.png')}" alt="edit css"/>
      </a>
      <a href="#" onclick="jQuery('#stylist_newclass').toggle('blind',500); return false;">
        <img src="${resource(dir:'images/icons/silk',file:'css_add.png')}" alt="add css class"/>
      </a>
    </nerderg:inputfield>

    <nerderg:inputfield label="Tag Style" field="target-style"/>
    <span class="button">
      <input id="stylist_edit" type='submit' value='change' />
    </span>
  </form>
  <div id="stylist_newclass" class="hidden form">
    <form id="stylist_insertform" action="style/insertStyle" method="post" >
      <nerderg:formfield label="Enter styles to insert in default CSS"/>
      <div>
        <g:textArea cols="40" rows="10" name="contentcss" value=".newclass {\n}"/>
      </div>
      <div class="buttons">
        <span class="button">
          <input id="stylist_insert" type="submit" value="add" />
        </span>
      </div>
    </form>
  </div>

</div>