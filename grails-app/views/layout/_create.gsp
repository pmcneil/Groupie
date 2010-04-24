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

            <g:form action="saveLayout" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:layoutInstance,field:'name')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fixed">Fixed:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'fixed','errors')}">
                                    <g:checkBox name="fixed" value="${layoutInstance?.fixed}" ></g:checkBox>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="width">Width:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'width','errors')}">
                                    <g:select from="${0..2000}" id="width" name="width" value="${layoutInstance?.width}" ></g:select>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="margin">Margin:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'margin','errors')}">
                                    <g:select from="${0..100}" id="margin" name="margin" value="${layoutInstance?.margin}" ></g:select>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sidebar">Sidebar:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'sidebar','errors')}">
                                    <g:select id="sidebar" name="sidebar" from="${layoutInstance.constraints.sidebar.inList}" value="${layoutInstance.sidebar}" ></g:select>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sidebarWidth">Sidebar Width:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:layoutInstance,field:'sidebarWidth','errors')}">
                                    <g:select from="${0..1000}" id="sidebarWidth" name="sidebarWidth" value="${layoutInstance?.sidebarWidth}" ></g:select>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button">
                      <input class="close" type="button" value="Create" onclick="return Groupie.postForm(this.form, EIP.log);"/>
                    </span>
                </div>
            </g:form>
