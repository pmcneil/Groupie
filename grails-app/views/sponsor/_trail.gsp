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
<ul id="trail">
    <li <g:if test="${step == '0'}">class="current"</g:if>>Introduction</li>
    <li <g:if test="${step == '1'}">class="current"</g:if>>Select Child</li>
    <li <g:if test="${step == '2'}">class="current"</g:if>>Details/Register</li>
    <li <g:if test="${step == '3'}">class="current"</g:if>>Confirm</li>
    <li <g:if test="${step == '4'}">class="end current"</g:if><g:else>class="end"</g:else>>Payment details</li>
  </ul>
