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

<%@ page import="com.nerderg.groupie.member.Member" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <util:css name="droppy" />
    <util:css name="boxy" />
    <util:css name="groupie" />
    <util:css name="smoothness/jquery-ui-1.7.2.custom" />

  <util:javascript src="jquery/jquery.droppy"/>
  <util:javascript src="jquery/jquery.boxy"/>
  <util:javascript src="ajaxupload.3.5"/>
  <util:javascript src="jquery/jquery.media"/>
  <util:javascript src="jquery/jquery.jSuggest.1.0"/>
  <util:javascript src="jquery/jquery-ui-1.7.2.custom.min"/>

  <script type="text/javascript">
    function postPost(ret) {
        jQuery('#userDetails').html(ret);
        dress();
    };
    function dress() {
        jQuery('#date').datepicker({dateFormat: 'yy-mm-dd', showOn: 'button', buttonImage: '${resource(dir:'images/icons/silk',file:'date.png')}', buttonImageOnly: true, changeMonth: true, changeYear: true});
        jQuery('#expiryDate').datepicker({dateFormat: 'yy-mm-dd', showOn: 'button', buttonImage: '${resource(dir:'images/icons/silk',file:'date.png')}', buttonImageOnly: true, changeMonth: true, changeYear: true});
        jQuery('.toggleNext').unbind('click').bind('click', toggleNext).next('div').hide();
    }
    function toggleNext() {
      jQuery(this).next('div').toggle('blind',500);
    }
    function reloadDetails(id) {
      Groupie.postData("${createLink(controller: 'user', action: 'eip')}", "id="+id, function(ret) {postPost(ret);});
    };
    $(function(){
      $('#userEntry').jSuggest({
          minchar: 1,
          delay: 100,
          loadingImg: "${resource(dir:'images',file:'spinner.gif')}",
          url: "${createLink(controller: 'user', action: 'suggest')}",
          type: "GET",
          data: "q",
          autochange: true,
          click: function(text) {
                var id = text.split(":", 1);
                reloadDetails(id);
              }
      });
      $('#userEntry').click(function(){$(this).val('')});
      dress();
      $('.clearfield').click(function(){$(this).prev('input').val('');});

    });
      
  </script>

  <title>Manage Users</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${resource(dir:'/')}">Home</a></span>
    <span class="menuButton"><g:link class="user" controller="user" action="manage">User Manager</g:link></span>
    <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
  </div>
  <div class="body">
    <h1>Manage Users</h1>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <nerderg:inputfield label="Find User" field="userEntry" size="40">
      <util:image class="imgbutton clearfield" src="icons/silk/cross.png" alt="clear" title="clear search"/>
    </nerderg:inputfield>
    <hr>
    <div id="userDetails">
      <util:logit message="Person is $person"/>
      <g:if test="${person}">
        <g:render template="editInPlace" model="['user':person]"/>
      </g:if>
    </div>
  </div>
</body>
</html>
