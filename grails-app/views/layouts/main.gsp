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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><g:layoutTitle default="Groupie" /></title>
    <META http-equiv="Content-Style-Type" content="text/css">
  <p:css name="reset" />
  <p:css name="base" />
  <p:css name="fonts" />
  <p:css name="main" />
  <p:css name="droppy" />
  <p:css name="boxy" />
  <p:css name="groupie" />
  <p:css name="jquery.timeentry" />
  <p:css name="smoothness/jquery-ui-1.7.2.custom" />
  <groupie:css names="['default']"/>

  <groupie:favicon src="images/icon32.png"/>
  <p:javascript src="jquery/jquery-1.4"/>
  <p:javascript src="jquery/jquery.droppy"/>
  <p:javascript src="jquery/jquery.boxy"/>
  <p:javascript src="ajaxupload.3.5"/>
  <p:javascript src="jquery/jquery.media"/>
  <p:javascript src="jquery/jquery.jSuggest.1.0"/>
  <p:javascript src="jquery/jquery.timeentry.min"/>
  <p:javascript src="jquery/jquery-ui-1.7.2.custom.min"/>
  <p:javascript src="application" />
  <g:layoutHead />

</head>
<body>
  <div id="spinner" class="spinner" style="display:none;">
    <img src="${resource(dir:'images',file:'spinner.gif')}" alt="Spinner" />
  </div>
  <div id="doc" class="template" style="background-color: white">
    <groupie:header name="header"></groupie:header>
    <div id="bd">
      <div id="main">
        <div>
          <g:layoutBody />
        </div>
      </div>

    </div>
    <hr/>
    <groupie:footer name="footer"></groupie:footer>
  </div>
</body>	
</html>