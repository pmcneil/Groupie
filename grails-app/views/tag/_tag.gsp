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
<div id="tagContent" class="boxy-content">
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>

  <g:form action="apply" method="post" onsubmit="return false;">
    <div class="dialog">
      <input type="hidden" id="id" name="id" value="${itemInstance.id}">
      <input type="text" id="tagEntry" name="tagEntry" class="tagEntry"/>
      <input type="button" class="addbutton" value="add"
             onclick="return Groupie.postForm(this.form, function(ret){jQuery('#tagContent').replaceWith(ret)});" />

    </div>
  </g:form>
  <h3>tags</h3>
  <g:each in="${itemInstance.tags}" var="tag">
    <span class="tag">${tag.name}
      <a href="${createLink(action:'remove', params:['id':itemInstance.id, 'tag': tag.id])}"
         onclick="return !Groupie.postData('${createLink(action:'remove', params:['id':itemInstance.id, 'tag': tag.id])}',
                                          null,
                                          function(ret){
                                            jQuery('#tagContent').replaceWith(ret);
                                          });">
        <p:image src="icons/silk/cross.png" />
      </a>
    </span>
  </g:each>
</div>
