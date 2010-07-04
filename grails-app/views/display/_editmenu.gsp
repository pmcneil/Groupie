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

<div id="${id}" class="${css}">
  <ul>
    <li>
      <a href="#"
         class="long"
         onclick="EIP.boxyToolbar();"
         title="Edit Tools">
        Toolbar
      </a>
    </li>
    <li>
      <a href="#" onclick="EIP.fireChanged(); return false;" title="Save ctrl-s">
        <util:image src="icons/silk/disk.png" width="16" height="16" alt="ctrl-s"/>
        <span class="long">Save</span>
        <span class="shortcut">[ctrl-s]</span>
      </a>
    </li>
    <li>
      <a href="#" onclick="EIP.editSource(); return false;" title="Edit source ctrl-e">
        <util:image src="icons/silk/page_white_code_red.png" alt="ctrl-e"/>
        <span class="long">Source</span>
        <span class="shortcut">[ctrl-e]</span>
      </a>
    </li>

    <li>
      <a href="#" onclick="EIP.executeCommand('undo'); return false;" title="Undo ctrl-z">
        <util:image src="icons/silk/arrow_undo.png" alt="Undo"/>
        <span class="long">Undo</span>
        <span class="shortcut">[ctrl-z]</span>
      </a>
    </li>
    <li>
      <a href="#" onclick="EIP.executeCommand('redo'); return false;" title="Redo ctrl-Z">
        <util:image src="icons/silk/arrow_redo.png" alt="Redo"/>
        <span class="long">Redo</span>
        <span class="shortcut">[ctrl-Z]</span>
      </a>
    </li>
    <li>
      <a href="#"
         onclick="EIP.boxyRevert();"
         title="Revert content">
        <util:image src="icons/silk/arrow_switch.png" alt="Revert"/>
        <span class="long">Revert</span>
      </a>
    </li>

    <li>
      <a href="#"
         onclick="EIP.boxyTag();"
         title="Tag content">
        <util:image src="icons/silk/comment.png" alt="Tag"/>
        <span class="long">Tag</span>
      </a>
    </li>

    <li>
      <a href="#"
         onclick="EIP.boxyInsertMedia();"
         title="Upload/Insert Media">
        <util:image src="icons/silk/picture_go.png" alt="Upload/Insert Media"/>
        <span class="long">Media</span>
      </a>
    </li>

    <li>
      <a class="subMenuHeading" name="edit_styles">Style</a>
      <ul>
        <li>
          <a href="#"
             onclick="EIP.boxyStylist();"
             title="Edit HTML tag and style">
            <util:image src="icons/silk/css.png" alt="Stylist"/>
            <span class="long">Stylist</span>
            <span class="shortcut">[alt-c]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('bold'); return false;" title="ctrl-b">
            <span class="long"><b>Bold</b></span>
            <span class="short"><b>B</b></span>
            <span class="shortcut">[ctrl-b]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('italic'); return false;" title="ctrl-i">
            <span class="long"><i>Italic</i></span>
            <span class="short"><i>I</i></span>
            <span class="shortcut">[ctrl-i]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('underline'); return false;" title="ctrl-u">
            <span class="long"><u>Underline</u></span>
            <span class="short"><u>U</u></span>
            <span class="shortcut">[ctrl-u]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('strikethrough'); return false;" title="ctrl-t">
            <span class="long"><strike>Strikethrough</strike></span>
            <span class="short"><strike>Strike</strike></span>
            <span class="shortcut">[ctrl-t]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('superscript'); return false;" title="ctrl-^">
            <span class="long"><sup>Superscript</sup></span>
            <span class="short"><sup>Super</sup></span>
            <span class="shortcut">[ctrl-^]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('subscript'); return false;" title="ctrl-6">
            <span class="long"><sub>Subscript</sub></span>
            <span class="short"><sub>Sub</sub></span>
            <span class="shortcut">[ctrl-6]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('removeformat'); return false;" title="ctrl-p">
            Plain
            <span class="shortcut">[ctrl-p]</span>
          </a>
        </li>
      </ul>
    </li>
    <li>
      <a class="subMenuHeading" name="edit_justification">Justify</a>
      <ul>
        <li><a href="#" onclick="EIP.executeCommand('justifyleft'); return false;" title="ctrl-left">
            <util:image src="icons/silk/text_align_left.png" alt="left"/>
            <span class="long">Left</span>
            <span class="shortcut">[ctrl-left]</span>
          </a>
        </li>
        <li><a href="#" onclick="EIP.executeCommand('justifycenter'); return false;" title="ctrl-up">
            <util:image src="icons/silk/text_align_center.png" alt="center"/>
            <span class="long">Center</span>
            <span class="shortcut">[ctrl-up]</span>
          </a>
        </li>
        <li><a href="#" onclick="EIP.executeCommand('justifyright'); return false;" title="ctrl-right">
            <util:image src="icons/silk/text_align_right.png" alt="right"/>
            <span class="long">Right</span>
            <span class="shortcut">[ctrl-right]</span>
          </a>
        </li>
        <li><a href="#" onclick="EIP.executeCommand('justifyfull'); return false;" title="ctrl-down">
            <util:image src="icons/silk/text_align_justify.png" alt="fully justify"/>
            <span class="long">Full</span>
            <span class="shortcut">[ctrl-down]</span>
          </a>
        </li>
      </ul>
    </li>
    <li>
      <a class="subMenuHeading" name="edit_heading">Headings</a>
      <ul>
        <li>
          <a href="#" onclick="EIP.executeCommand('formatblock','<h1>'); return false;" title="alt-1">
            <span class="long">Heading 1</span>
            <span class="short">H1</span>
            <span class="shortcut">[alt-1]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('formatblock','<h2>'); return false;" title="alt-2">
            <span class="long">Heading 2</span>
            <span class="short">H2</span>
            <span class="shortcut">[alt-2]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('formatblock','<h3>'); return false;" title="alt-3">
            <span class="long">Heading 3</span>
            <span class="short">H3</span>
            <span class="shortcut">[alt-3]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('formatblock','<h4>'); return false;" title="alt-4">
            <span class="long">Heading 4</span>
            <span class="short">H4</span>
            <span class="shortcut">[alt-4]</span>
          </a>
        </li>
      </ul>
    </li>
    <li>
      <a class="subMenuHeading" name="edit_content">Content</a>
      <ul>
        <li>
          <a href="#" onclick="EIP.insertLinkFromSelection(); return false;" title="Link ctrl-`">
            <util:image src="icons/silk/link.png" alt="ctrl-`"/>
            <span class="long">Insert Link</span>
            <span class="shortcut">[ctrl-`]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('insertunorderedlist'); return false;" title="Bullet list ctrl-*">
            <util:image src="icons/silk/text_list_bullets.png" alt="ctrl-*"/>
            <span class="long">Unorderd List</span>
            <span class="shortcut">[ctrl-*]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('insertorderedlist'); return false;" title="Numbered list ctrl-#">
            <util:image src="icons/silk/text_list_numbers.png" alt="ctrl-#"/>
            <span class="long">Orderd List</span>
            <span class="shortcut">[ctrl-#]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.executeCommand('inserthorizontalrule'); return false;" title="Horiz rule ctrl-_">
            <util:image src="icons/silk/text_horizontalrule.png" alt="ctrl-_"/>
            <span class="long">Horiz Rule</span>
            <span class="shortcut">[ctrl-_]</span>
          </a>
        </li>
        <li>
          <a href="#"
             onclick="EIP.boxyInsertContent();"
             title="Insert Content/Menu/Module">
            <util:image src="icons/silk/brick_go.png" alt="Insert Content/Menu/Module"/>
            <span class="long">Insert Content</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.insertContentFromSelection(); return false;"
             title="Insert or create content named from the selection [alt-7]">
            <util:image src="icons/silk/arrow_in.png" alt="alt-7"/>
            <span class="long">From selection</span>
            <span class="shortcut">[alt-7]</span>
          </a>
        </li>
        <li>
          <a href="#" onclick="EIP.boxyWrapDiv(); return false;" title="Wrap selection with div">
            <span class="long">Wrap in div</span>
            <span class="short">&lt;div&gt;</span>
          </a>
        </li>
      </ul>
    </li>
  </ul>
</div>
