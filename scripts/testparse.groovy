/*
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
*/

import org.htmlcleaner.*

def testHtml = """<groupie:header name="header"/>
<groupie:nav name="nav"/>
<groupie:bd name="\${pageName}">
  <groupie:sidebar name="sidebar"/>
</groupie:bd>
<groupie:footer name="footer"/>"""
//        ContentService contentService = new ContentService()
CleanerProperties props = new CleanerProperties()
props.setAllowHtmlInsideAttributes(true)
props.setUseEmptyElementTags(true)
props.setEscapeAttributes(false)
HtmlCleaner cleaner = new HtmlCleaner(props)
def node = cleaner.clean(testHtml)
def body = node.findElementByName("body", false)
def kids = node.getChildren()
kids.each{
    println it
}
println cleaner.getInnerHtml(body)
