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
<%@ page contentType="text/css;charset=UTF-8" %>

/*
Copyright (c) 2009, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 2.7.0
 */
/**
 * YUI Grids
 * @module grids
 * @namespace yui-
 * @requires reset, fonts
 */

/**
 * Note: Throughout this file, the *property (star-property) filter is used
 * to give a value to IE that other browsers do not see. _property is only seen
 * by IE7, so the combo of *prop and _prop can target between IE6 and IE7.
 *
 * More information on these filters and related validation errors:
 * http://tech.groups.yahoo.com/group/ydn-javascript/message/40059
 */

/**
 * Section: General Rules
 */

body {
  text-align: center;
  }

/**
* Section: Page Width Rules (#doc)
*/

#doc, .template {
	margin: auto;
	text-align: left;
	width: 57.69em;
	*width: 56.25em;
}

#doc {
/**
* Left and Right margins are not a structural part of Grids. Without them
* Grids works fine, but content bleeds to the very edge of the document, which
* often impairs readability and usability. They are provided because they
* prevent the content from "bleeding" into the browser's chrome.
*/
<g:if test="${style.fixed}">
	width: ${style.width / 13.000}em;
	*width: ${style.width / 13.333}em;
</g:if>
<g:else>
	margin: auto ${style.margin}px;
  width: auto;
  min-width: ${style.width / 13.000}em;
</g:else>
}


.block {
	/* to preserve source-order independence for Gecko */
	position: relative;
}

.block {
	/* to preserve source-order independence for IE */
	_position: static;
}

#main .block {
	/* to preserve source-order independence for Gecko */
	position: static;
}

#main {
	width: 100%;
}
<g:set var="mainpos" value="${style.sidebar == 'left' ? 'right' : 'left'}"/>
/* where the main section goes */
.template #main {
	float: ${mainpos};
	/* IE: preserve layout at narrow widths */
	margin-${style.sidebar}: -25em;
}
/* where sidebar goes and widths */
.template .block {
	float: ${style.sidebar};
	width: ${style.sidebarWidth / 13.000}em;
	*width: ${style.sidebarWidth / 13.333}em;
}

.template #main .block {
	margin-${style.sidebar}: ${(style.sidebarWidth / 13.000) + 1 }em;
	*margin-${style.sidebar}: ${(style.sidebarWidth / 13.333) + 1 }em;
}

#main .block {
	float: none;
	width: auto;
}

/** grids *** */

.grid, .half, .third, .full {
float: left;
  overflow: auto;
}

.grid .full {
  width: 97%;
  margin-right: 0;
}

.grid .half{
  width: 47%;
  margin-right: 7px;
}

.grid .third{
  width: 31%;
  margin-right: 15.5px;
}

/* group Clearing */
#hd:after,
#bd:after,
#ft:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

#hd,
#bd,
#ft {
	zoom: 1;
}
