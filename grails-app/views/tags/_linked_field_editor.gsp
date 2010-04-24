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

<!--
This edits a field linked to a domain object. You need to implement a template form editor
called _controller_form.gsp in the view/controller directory.
The controller needs to implement ajaxUpdate and ajaxSave actions

need:
  label
  field - the (list) field of the owning instance that we are editing
  inst - the owning instance
  instName - the name of the inst class
  controller - controller to send the updated info
  reload - javascript function to call on reload
-->
<g:set var="lowerInstName" value="${instName.toLowerCase()}"/>

<nerderg:formfield label="${label}" field="${field}" bean="${inst}">
  <ul>
    <g:each var="item" in="${inst[field]}">
      <li>
        <a href="#" onclick="jQuery('#${field}${item.id}').toggle('blind',500); return false;">${item?.encodeAsHTML()}</a>
      </li>
    </g:each>
    <li>
      <a href="#" onclick="jQuery('#add${field}').toggle('blind',500); return false;">Add ${label}</a>
    </li>
  </ul>
  <div>
    <g:each var="item" in="${inst[field]}">
      <div id="${field}${item.id}" class="hidden form">
        <g:form controller="${controller}" action="ajaxUpdate" method="post" onsubmit="return false;">
          <input type="hidden" name="id" value="${item?.id}" />
          <input type="hidden" name="version" value="${item?.version}" />
          <input type="hidden" name="${lowerInstName}.id" value="${inst?.id}" />
          <div class="dialog">
            <g:render template="/${controller}/${controller}_form" model="['inst': item]"/>
          </div>
          <div class="buttons">
            <span class="button">
              <g:actionSubmit class="save" value="Update"
                              onclick="Groupie.postData(this.form.action, jQuery(this.form).serialize(),
                                       function(d, opts){
                                         Groupie.status(d, opts);
                                         if(!d.errors) {
${reload}
                                          }
                                       }, false, {el: this})"/>
            </span>
          </div>
        </g:form>
      </div>
    </g:each>

    <div id="add${field}" class="hidden form">
      <g:form controller="${controller}" action="ajaxSave" method="post" onsubmit="return false;">
        <input type="hidden" name="${lowerInstName}.id" value="${inst?.id}" />
        <div class="dialog">
          <g:render template="/${controller}/${controller}_form" model="['inst': null]"/>
        </div>
        <div class="buttons">
          <span class="button">
            <g:actionSubmit class="save" value="Add"
                            onclick="Groupie.postData(this.form.action, jQuery(this.form).serialize(),
                                       function(d, opts){
                                         Groupie.status(d, opts);
                                         if(!d.errors) {
${reload}
                                          }
                                       }, false, {el: this})"/>
          </span>
        </div>
      </g:form>
    </div>
  </div>

</nerderg:formfield>
