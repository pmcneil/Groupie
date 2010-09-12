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
  admin menu tag
-->

<%@ page contentType="text/html;charset=UTF-8" %>
<g:if test="${model}">
  <g:ifAnyGranted role="ROLE_ADMIN,ROLE_EDITOR">
    <g:set var="page" value="${model.content}"/>
    <ul class="dropmenu admin">
      <li id="AdminMenu">
        <a id="edit_menu" name="edit_menu">edit</a>
        <ul>
          <li>
          <g:if test="${model.writable}">
            <a href="#EditContent" onclick="EIP.editMode(); return false;">
              Content
            </a>
          </g:if>
          <g:else>
            <a class="disabled" name="edit_menu_content"
               title="You're not authorised to edit this page.">Content</a>
          </g:else>
      </li>
      <li>
      <g:if test="${model.writable}">
        <a href="${g.createLink(controller: 'page', action: 'editPage', id: page.id)}"
           class="boxymodal subMenuHeading"
           title="Edit this page">
          Page
        </a>
      </g:if>
      <g:else>
        <a class="disabled subMenuHeading" name="edit_menu_page"
           title="You're not authorised to edit this page.">Page</a>
      </g:else>
      <ul>
        <li>
          <a href="${g.createLink(controller: 'page', action: 'createPage')}"
             class="boxymodal"
             title="Create a new page">
            New Page
          </a>
        </li>
        <li>
          <a href="${g.createLink(controller: 'page', action: 'createTemplate')}"
             class="boxymodal"
             title="Create a new page template">
            New Template
          </a>
        </li>
        <li>
          <a href="${g.createLink(controller: 'layout', action: 'createLayout')}"
             class="boxymodal"
             title="Create a new page layout">
            New Layout
          </a>
        </li>

        <li>
        <g:if test="${model.writable}">
          <a href="${g.createLink(controller: 'style', action: 'editPageStyle', params: ['name': page.item.name])}"
             class="boxy"
             title="Edit page style">
            Style
          </a>
        </g:if>
        <g:else>
          <a class="disabled" name="edit_menu_style"
             title="You're not authorised to edit this page.">Style</a>
        </g:else>
        </li>
        <li>
        <g:if test="${model.writable}">
          <a href="#"
             onclick="EIP.boxyTag('${page.item.id}');"
             title="Tag content">
            <util:image src="icons/silk/comment.png" alt="Tag"/>
            <span class="long">Tag</span>
          </a>
        </g:if>
        <g:else>
          <a class="disabled" name="edit_menu_style"
             title="You're not authorised to edit this page.">Tag</a>
        </g:else>
        </li>
      </ul>

      </li>
      <li>
        <a href="${g.createLink(controller: 'menu', action: 'list')}"
           class="boxy"
           title="Edit menus">
          Menus
        </a>
      </li>
      <li>
        <a href="${g.createLink(controller: 'module', action: 'list')}"
           class="boxy"
           title="Edit modules">
          Modules
        </a>
      </li>
      <li>
        <a href="${g.createLink(controller: 'macro', action: 'list')}"
           class="boxy"
           title="Edit macros">
          Macros
        </a>
      </li>
      <li>
        <a href="${g.createLink(controller: 'script', action: 'list')}"
           class="boxy"
           title="Edit scripts">
          Scripts
        </a>
      </li>
      <li>
        <a href="${g.createLink(controller: 'style', action: 'editPageStyle', params: ['name': 'default'])}"
           class="boxy"
           title="Edit styles">
          Styles
        </a>
      </li>
      <g:ifAllGranted role="ROLE_ADMIN">
        <li>
          <a href="${g.createLink(controller: 'user', action: 'manage')}">Users</a>
        </li>
        <li>
          <a href="${g.createLink(controller: 'role', action: 'list')}">Roles</a>
        </li>
        <li>
          <a href="${g.createLink(controller: 'event', action: 'list')}">Events</a>
        </li>
      </g:ifAllGranted>
      <li>
        <a href="${g.createLink(controller: 'help', action: 'edit')}"
           class="boxy"
           title="Get some help man.">
          Help
        </a>
      </li>
    </ul>
    </li>
    </ul>
  </g:ifAnyGranted>
</g:if>
