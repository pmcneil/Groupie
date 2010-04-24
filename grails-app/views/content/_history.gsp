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
<div id="revertContent">
<g:form action="revert" method="post">
  <div class="dialog">
    <input type="hidden" name="id" value="${contentInst.id}">

    <nerderg:formfield label="Revert To">
      <g:select optionKey="id"
                from="${contentInst.item.content?.sort{ def a, def b -> b.id <=> a.id }}"
                optionValue='${{it.id + ":" + it.title.encodeAsHTML() + " edited " + prettytime.display(date: it.lastUpdated) + " by " + it.author.username.encodeAsHTML()}}'
                name="currentId"
                value="${contentInst.item?.current?.id}"
                noSelection="['null':'']"
                onChange="return Groupie.postForm(this.form, function(ret){window.location.reload();});"></g:select>
    </nerderg:formfield>
  </div>
</g:form>
</div>