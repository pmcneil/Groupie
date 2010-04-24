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
<html>
  <head>
    <groupie:css names="${model.cssnames}"/>
    <groupie:script names="${model.scriptnames}"/>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="layout" content="dynamic" />
  <link rel="stylesheet" href="${g.createLink(controller:'layout',action:'gridcss', params: [layout: model.layout])}" type="text/css"/>
  <g:set var="page" value="${model.content}"/>
  <g:set var="params" value="${model.params}"/>
  <title>${page.title}</title>
</head>
<body>
  <groupie:processContent model="${model}"/>
</body>
</html>
