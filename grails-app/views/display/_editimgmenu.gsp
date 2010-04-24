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

<div id="editImgMenu" class="${css}">
  <ul>
    <li>
      <a href="#editImgForm" class="boxy" title="Resize Image">
        Resize
        <span class="shortcut">[ctrl-r]</span>
      </a>
    </li>
    <li>
      <a href="#"
         onclick="EIP.boxyStylist();"
         title="Edit HTML tag and style">
        <p:image src="icons/silk/css.png" alt="Stylist"/>
        <span class="long">Stylist</span>
        <span class="shortcut">[alt-c]</span>
      </a>
    </li>
    <li>
      <a href="#" onclick="EIP.boxyWrapDiv(); return false;" title="Wrap selection with div">
        <span class="long">Wrap in div</span>
        <span class="short">&lt;div&gt;</span>
      </a>
    </li>

  </ul>
</div>

<g:form name="editImgForm" action="savePage" method="post" style="display:none;">
  <nerderg:formfield label="Width">
    <input name='w' type='text' value=''/>
  </nerderg:formfield>
  <nerderg:formfield label="Height">
    <input name='h' type='text' value=''/>
  </nerderg:formfield>
  <nerderg:formfield label="Original aspect?">
    <g:checkBox name='a' value='${true}'/>
  </nerderg:formfield>
  <div class="buttons">
    <span class="button">
      <input class="resize" type="button" value="Full" onclick="this.form.w.value=''; this.form.h.value='';EIP.resizeImage();"/>
      <input class="resize" type="button" value="Resize" onclick="EIP.resizeImage();"/></span>
  </div>
</g:form>
