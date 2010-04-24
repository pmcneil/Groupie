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
<%@ page import="com.nerderg.groupie.content.Item" %>

<div id="insert_content" class="hidden form">
  <form action="content/suggestContent" method="POST" onsubmit="return false;">
    <nerderg:formfield label="Type">
      <g:select id="insert_content_type" name="type" from="${['Content', 'Menu', 'Module', 'Macro']}"/>
    </nerderg:formfield>
    <nerderg:inputfield class="tagEntry" label="Tagged" id="insert_content_tags" field="tagEntry" />
  </form>
  <hr/>
  <form action="">
    <nerderg:formfield label="Insert">
      <span id="insert_content_select">
        <g:select id="insert_content_item" name="item" from="${Item.findAllByType('content')}"
                  optionKey="id"
                  optionValue="name"
                  style="width: 15em;"></g:select>
      </span>
      <input id="insert_content_button" type="button" class="addbutton" value="insert ->" />
    </nerderg:formfield>
  </form>
</div>