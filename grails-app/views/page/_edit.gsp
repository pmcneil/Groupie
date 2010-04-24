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

<g:form action="updatePage" method="post">
  <div class="dialog">
    <input type="hidden" name="id" value="${pageInst.id}">
    <g:render template="pageForm" model="['pageInst': pageInst]"/>
  </div>
  <div class="buttons">
    <span class="button"><input class="save" type="submit" value="Update" /></span>
  </div>
</g:form>
<g:form action="revertPage" method="post">
  <div class="dialog">
    <hr/>
    <input type="hidden" name="id" value="${pageInst.id}">

    <nerderg:formfield label="Revert To">
      <g:select optionKey="id"
                from="${pageInst.item.content.sort{ def a, def b -> b.id <=> a.id }}"
                optionValue='${{it.id + ":" + it.title.encodeAsHTML() + " edited " + prettytime.display(date: it.lastUpdated) + " by " + it.author.username.encodeAsHTML()}}'
                name="currentId"
                value="${pageInst.item?.current?.id}"
                noSelection="['null':'']"
                onChange="this.form.submit()"></g:select>
    </nerderg:formfield>
    <hr/>
  </div>
</g:form>
