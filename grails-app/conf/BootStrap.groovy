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

import com.nerderg.groupie.*
import com.nerderg.groupie.content.*
import com.nerderg.groupie.member.*
import com.nerderg.groupie.donate.*
import com.nerderg.groupie.event.*
import grails.util.Environment
import java.sql.SQLException

class BootStrap {
    def authenticateService
    def init = { servletContext ->

        switch (Environment.current) {

            case Environment.DEVELOPMENT:
            createBaseRolesIfRequired()
            createAdminUserIfRequired()
            createEditorUserIfRequired()
            addRequestMapsIfRequired()
            if(Item.count() == 0){
                createDefaultContent()
            }
            createMemberTypesIfRequired()
            createMemberFlagsIfRequired()
            createDataTagIfRequired()
            break;
            case Environment.PRODUCTION:
            createBaseRolesIfRequired()
            createAdminUserIfRequired()
            createEditorUserIfRequired()
            addRequestMapsIfRequired()
            if(Item.count() == 0){
                createDefaultContent()
            }
            createMemberTypesIfRequired()
            createMemberFlagsIfRequired()
            createDataTagIfRequired()
            break;
        }
        //avoid save bug
        Content.count()
        Item.count()
        Layout.count()
        Media.count()
        Tag.count()
        Requestmap.count()
        Role.count()
        GUser.count()
        Event.count()
        Rsvp.count()

    }
    def destroy = {
    }

    void createDefaultContent() {
        def menu = createContent('Main Menu', 'menu', 'Main Menu', 'text/html',
"""
<div class="navigation">
  <ul class="dropmenu">
    <groupie:pages name="Main Menu"></groupie:pages>
    <li>
    <a name="sites">sites</a>
        <ul>
            <li><a href="http://abc.net.au" target="extlink">abc</a></li>
            <li><a href="http://mcneils.net" target="extlink">mcneils</a></li>
            <li><a href="http://nerderg.com" target="extlink">Nerderg</a></li>
        </ul>
    </li>
  </ul>
  <groupie:login></groupie:login>
  <groupie:admin></groupie:admin>
</div>

""")
        createContent('footerMenu', 'menu', 'Footer Menu', 'text/html',
"""
<div class="footerMenu">
<ul class="menu">
  <groupie:pages name="footerMenu"></groupie:pages>
  <li><a href="http://nerderg.com">nerdErg</a>
  </li>
</ul>
  <groupie:login></groupie:login>
  <groupie:admin></groupie:admin>
</div>
""")

        def sidebarMenu = createContent('sidebar', 'menu', 'Sidebar Menu', 'text/html',
"""
<div class="sidebarMenu">
<ul>
  <groupie:pages name="sidebar"></groupie:pages>
    
    <li><a href="<g:createLink controller='register' action='reg'/>"
           class="boxy"
           title="Register on site.">
        register
      </a>
    </li>
    <li><a href="<g:createLink controller='member' action='join'/>"
           class="boxy"
           title="Become a member.">
        Join
      </a>
    </li>
</ul>
</div>
""")

        createContent('menu template', 'menu', 'Sidebar Menu', 'text/html',
"""
<div class="\${menuNameClass}">
<ul>
  <groupie:pages name="\$menuName"/>
  <li><a href="http://nerderg.com">nerdErg</a>
  </li>
</ul>
</div>
""")


        createContent('Default Page Template', 'template', 'Default Page Template', 'text/html',
"""<groupie:header name="header"/>
<groupie:menu name='Main Menu'/>
<groupie:bd name="\${pageName}">
  <groupie:sidebar name="sidebar"/>
</groupie:bd>
<groupie:footer name="footer"/>
""")

        def home = createContent('home', 'page', 'Home Page', 'text/html',
"""<groupie:header name="header"/>
<groupie:menu name='Main Menu'/>
<groupie:bd name="main">
  <groupie:sidebar name="sidebar"/>
</groupie:bd>
<groupie:footer name="footer"/>
""")
        home.parent = menu
        menu.save()
        createContent('main', 'content', 'Home page content', 'text/html',
"""
      <h1>Quick Start</h1>
<div class="full">
<p>Groupie is a WYSIWYG CMS mated with mailing list/forum etc. for your Group.</p>
<p>Simply log in as admin with password admin and you'll see a new edit menu
come up. From there you can edit the content on this page. Just click
on the content in edit mode and start typing. You can format the text
using the right click (context) menu or select the Toolbar option to
open an easy to use toolbar.</p><p>Content changes are saved when you click on
some other content area or select <span style="font-weight: bold;">edit-&gt;Stop Editing</span></p>
<div class="tip">
<h1>TIP</h1>
<ol>
<li>There are keyboard shortcuts for most editing options, learn them to get really quick editing results.</li>
<li>Change the admin and editor passwords via edit-&gt;user to stop being hacked!</li>
</ol>
</div>
</div>
<div class="half"><div class="pad">
<h3>This is a column</h3>
Groupie has pre set the
Styles needed to make simple double or triple columns just click insert
content (the lego brick icon) and select the two column macro.<br><br>
You can edit your own macros using the edit menu.<br>
<h3>Pages</h3>
You can add pages by using the <span style="font-weight: bold;">edit-&gt;Page-&gt;New Page</span>
menu option. Make sure you set a parent like Main Menu to get it to
show on a menu (otherwise just type the page name after the URL.<br>
<h3>Layouts</h3>You can simply define a layout via <span style="font-weight: bold;">edit-&gt;Page-&gt;New Layout</span>.
Layouts use a heavily modified YUI grid that is dynamically set-up.<br>
<h3>Templates</h3>
You can modify the default page sections and template via <span style="font-weight: bold;">edit-&gt;Page-&gt;New Template</span>
or directly in the page using the <span style="font-weight: bold;">edit-&gt;Page</span> dialog, click on the + and edit the template.<br>
</div>
</div>
<div class="half"><div class="pad">
<h3>Content types</h3>
Groupie has a number of different types of content.<br>
<ul>
<li>Styles - css style sheets which allow variables and dynamic content</li>
<li>Menus - you can define different menus that you can then assign pages to</li>
<li>Scripts - javascript the default javascript is provided to all pages but if you
make a script with the name of the page it will be available to that
page only</li>
<li>Modules - a GSP fragment that is inserted as a
non-editable fragment into the document good for using groupie tags
un-expanded so they won't be accidentally edited by people entering
content.</li>
<li>Macros - html snippets that are simply inserted and directly editable by content editors - like these columns.</li>
</ul>
<h3>Content styling tips</h3>
Open the <span style="font-weight: bold;">Stylist</span>
using the toolbar or context menu then click on a section of the
content to see the wrapping html tag element. You can type in element
styles, add&nbsp; class and even enter in a new class for the default style
sheet.<br>
<h3>Source editing</h3>For when you need to get down and
dirty with the code (which will be too often until we iron out the
wrinkles) select edit source form the <br>context menu or toolbox.
</div>
</div>
""")
        createContent('header', 'header', 'Default header', 'text/html',
"""<img src="\${resource(dir:'images',file:'groupie_logo.jpg')}" alt="Groupie" /> Under new management""")
        createContent('footer', 'footer', 'Default Footer', 'text/html', """<groupie:menu name="footerMenu"/>Copyright © Nerderg """)
        createContent('sidebar', 'content', 'Default Sidebar', 'text/html',
"""<h1>Sidebar</h1>
<groupie:menu name="sidebar"/>
<p>This is your side bar, you can put stuff here. See edit->page and edit->page->New Layout to change the layout of this page.</p>
<p>The page uses a modified YUI layout that is dynamic. You can also have columns anywhere you like (see the source in the main content).</p>
""")
        def events = createContent('events', 'page', 'Events', 'text/html',
"""<groupie:header name="header"/>
<groupie:menu name='Main Menu'/>
<groupie:bd name="events">
  <groupie:sidebar name="sidebar"/>
</groupie:bd>
<groupie:footer name="footer"/>
""")
        events.parent = sidebarMenu
        sidebarMenu.save()
createContent('events', 'content', 'events', 'text/html',
"""<h1>events</h1>
<groupie:module name="Event list" />
""")

        createContent('default', 'style', 'Default style', 'text/css',
"""/* This is the site wide CSS */

\${g.set(var: 'p1', value: 'white')}
\${g.set(var: 's1', value: 'darkgray')}
\${g.set(var: 's2', value: 'lightblue')}
\${g.set(var: 'c1', value: 'gray')}
\${g.set(var: 'text', value: 'black')}
\${g.set(var: 'mutedtext', value: '#888')}

body {
  color: \${text};
  background: \${p1};
}

h1, h2, h3, h4, h5, h6 {
  color: \${c1};
}

blockquote {
  white-space: pre;
}

#console {
  height: 100px;
  overflow: auto;
}

.navigation { background-color: \${s2}; height: 2em; }

/* Essentials - configure this */

.dropmenu ul { width: 10em; }
.dropmenu ul ul { left: 10.1em; }

/* Everything else is theming */

.dropmenu { background-color: \${s2}; height: 2em; display: inline-block;}
.dropmenu *:hover { background-color: none; }
.dropmenu a { border-right: 1px solid white; color: white; font-size: 1em; padding: 0.5em; line-height: 1; }
.dropmenu li { width: 10em; }
.dropmenu li.hover a { background-color: \${c1}; }
.dropmenu ul { top: 2.1em; }
.dropmenu ul li a { background-color: \${c1}; }
.dropmenu ul a.hover { background-color: \${s2}; }
.dropmenu ul a { border-bottom: 1px solid white; border-right: none; opacity: 0.9; filter: alpha(opacity=90); }

.dropmenu.login { float: right;}
.dropmenu.admin { float: right;}

.footerMenu .dropmenu.login { float: none; }
.footerMenu .dropmenu.admin { float: none; }

/* footerMenu */
.footerMenu {text-align: center; padding: 10px 0;}
.footerMenu ul.dropmenu { border: 1px \${mutedtext} solid; }
.footerMenu .dropmenu { display: inline-block; background-color: \${p1}; height: 2.0em; text-align: left;}
.footerMenu .dropmenu li { width: 10em; }
.footerMenu .dropmenu ul { top: 2.1em; width: 10em;}
.footerMenu .dropmenu ul ul { left: 8.1em; top: -1.1em; }
.footerMenu .dropmenu *:hover { background-color: transparent; }
.footerMenu .dropmenu a { border: none; color: \${mutedtext}; font-size: 1em; padding: 0.5em; line-height: 1; }
.footerMenu .dropmenu li.hover a { background-color: \${p1}; }
.footerMenu .dropmenu ul li a { background-color: \${p1}; }
.footerMenu .dropmenu ul a.hover { background-color: \${p1}; border: 1px \${mutedtext} solid; }
.footerMenu .dropmenu ul a {border:  none; opacity: 1.0; filter: alpha(opacity=100);}

/* sidebar menu */

.sidebarMenu {
    background: \${p1};
    color: \${c1};
    width: 10em;
    margin: 1em;
}

.sidebarMenu .short {
    display: none;
}

.sidebarMenu ul {
    margin: 0;
    padding: 0;
}

.sidebarMenu ul li,
.sidebarMenu ul li ul li {
    list-style: none;
    background: \${p1};
    padding: 0.5em;
    display: block;
    text-align: left;
    border-bottom: solid \${c1} 1px;
}

.sidebarMenu ul li:hover,
.sidebarMenu ul li:hover ul li:hover
{
    background-color: \${s1};
}

.sidebarMenu li:hover a,
.sidebarMenu li:hover ul li:hover a
{
    background-color:\${s1};
}

.sidebarMenu li:hover ul li a
{
    background-color: \${p1};
}

.sidebarMenu a,
.sidebarMenu li ul li a{
    color: \${c1};
    font-size: 1em;
    line-height: 1;
    text-decoration: none;
    font-weight: bold;
    display: block;
}

.sidebarMenu li ul{
    background: \${p1};
    width: 10em;
    margin-top: 0;
    display: none;
}

.sidebarMenu li:hover ul{
    display: block;
}

.sidebarMenu li:hover li ul
{
    display: block;
}

.sidebarMenu li:hover li:hover ul
{
    display: block;
}

.article {
    margin: 2em;
    text-align: justify
}
.logo img {
    padding:0 10px;
    vertical-align:middle;
}
.pad {
    padding: 10px;
}
ul.eventList li {
    list-style: none;
    margin-bottom: 20px;
}
ul.eventList ul li {
    list-style: disc outside;
    margin-bottom: 0;
}

.eventTitle {
    font-weight: bold;
    font-size: 138.5%;
    background: #EDEDFF;
    margin: 1em 0;
    display: block;
}
.eventDescription{
    margin: 10px;
    padding: 10px;
    border: 1px solid lightgrey;
}
.eventDate {
    display: block;
}
.eventLocation{
    display: block;
}
.eventThanks {
    background: #f3f8fc url(../images/skin/information.png) 8px 50% no-repeat;
    border: 1px solid #b2d1ff;
    color: #006dba;
    margin: 10px 0 5px 0;
    padding: 5px 5px 5px 30px;
}
""")

        createContent('Two columns', 'macro', 'Two columns', 'text/html',
"""<div class="half">
    <div class="article">
        Column 1
    </div>
</div>
<div class="half">
    <div class="article">
        Column 2
    </div>
</div>
""")

        createContent('Three columns', 'macro', 'Three columns', 'text/html',
"""<div class="third">
    <div class="article">
        Column 1
    </div>
</div>
<div class="third">
    <div class="article">
        Column 2
    </div>
</div>
<div class="third">
    <div class="article">
        Column 3
    </div>
</div>
""")
        createContent('Rounded Box', 'macro', 'Rounded box', 'text/html',
"""<div style="background: yellow;">
<img style="float: left;" id="" class="" src="images/tl.png">
<img style="float: right;" id="" class="" src="images/tr.png">&nbsp;
<div class="pad"><h2 style="margin-top: 0;" id="" class="">Heading</h2>
Insert Content Here
</div>
&nbsp;
<img style="float: left;" id="" class="" src="images/bl.png">
<img style="float: right;" id="" class="" src="images/br.png">
</div>
""")

        createContent('Event list', 'module', 'Event list', 'text/html',
"""
<g:if test="\${flash.message}">
    <div class="message">\${flash.message}</div>
</g:if>

<ul class="eventList">
  <g:each in="\${com.nerderg.groupie.event.Event.findAll('from Event as e where startDate > :now order by startDate', [now: new Date()])}" var="event">
    <li>
        <span class="eventTitle">\${event?.name.encodeAsHTML()}</span>
        <span class="eventDate"><b>When:</b> <groupie:dateFormat format="EEE, MMM, d h:mm a" date="\${event?.startDate}"/></span>
        <span class="eventLocation"><b>Where:</b> \${event?.location.encodeAsHTML()}</span>
        <event:hasNotRsvped event="\${event}">
        <div style="margin-bottom: 10px;">
          <b>RSVP:</b>&nbsp;<g:link controller="rsvp" action="yes" id="\${event.id}">Click Here to RSVP</g:link>
        </div>
        </event:hasNotRsvped>
        <div class="toggleNext">
            <b>Description:</b>
        </div>
        <div class="eventDescription">
          <groupie:show name="\${event?.description}" />
        <div>
    </li>
  </g:each>
</ul>

""")

        createContent('News module', 'module', 'News Module', 'text/html',
"""
<groupie:showtagged tag="news" limit="3"/>
""")

        createContent('Member introduction', 'content', 'Member introduction', 'text/html',
"""<h1>Member introduction</h1>
""")
        createContent('Thanks for joining', 'content', 'Thanks for joining', 'text/html',
"""<h1>Thanks for joining</h1>
""")
        createContent('Sponsor Introduction', 'content', 'Sponsor Introduction', 'text/html',
"""<h1>Sponsor Introduction</h1>
""")
        createContent('No sponsorships available', 'content', 'No sponsorships avaliable', 'text/html',
"""<h1>No sponsorships avaliable</h1>
""")
        createContent('Payment Details', 'content', 'Payment Details', 'text/html',
"""<h1>Payment Details</h1>
""")

    }

    private Item createContent(String name, String type, String title, String mimetype, String content) {
        def user = GUser.findByUsername("admin")
        def item = new Item(name: name, type: type)
        save item
        def cnt = new Content(mimeType: mimetype, title: title,
            content: content, author: user, item: item)
        save cnt
        item.current = cnt
        item.save(flush: true)
    }

    void createBaseRolesIfRequired() {
        if(!Role.findByAuthority("ROLE_ADMIN")){
            def adminRole = new Role(authority: "ROLE_ADMIN", description: "Administrator")
            save adminRole
            def editorRole = new Role(authority: "ROLE_EDITOR", description: "Content editor")
            save editorRole
            def userRole = new Role(authority: "ROLE_USER", description: "Registered user")
            save userRole
        }
    }

    void createAdminUserIfRequired() {
        if(!GUser.findByUsername("admin")) {
            def user = new GUser(username: "admin", passwd: authenticateService.encodePassword("admin"),
                email: "admin@nerderg.com", salutation: "Mr", firstName: "Peter", lastName: "McNeil", enabled: true)
            save(user)
            Role.findByAuthority("ROLE_ADMIN").addToPeople(user)
        } else {
            println "Existing admin user"
        }
    }

    void createEditorUserIfRequired() {
        if(!GUser.findByUsername("editor")) {
            def user = new GUser(username: "editor", passwd: authenticateService.encodePassword("editor"),
                email: "editor@nerderg.com", salutation: "Mr", firstName: "Peter", lastName: "McNeil", enabled: true)
            save(user)
            Role.findByAuthority("ROLE_EDITOR").addToPeople(user)
        } else {
            println "Existing editor user"
        }
    }

    void addRequestMapsIfRequired() {
        if(!Requestmap.findByUrl("/**")) {
            addMapping([url: "/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY, IS_AUTHENTICATED_REMEMBERED"])

            addMapping([url: "/login/*", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])

            addMapping([url: "/admin/**", configAttribute: "ROLE_ADMIN"])

            addMapping([url: "/content/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/content/show", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])
            addMapping([url: "/content/byname", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])

            addMapping([url: "/item/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/layout/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/layout/gridcss", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])
            addMapping([url: "/media/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/media/show/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])
            addMapping([url: "/media/content/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])
            addMapping([url: "/menu/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/page/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/style/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/tag/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/script/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/module/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])
            addMapping([url: "/macro/**", configAttribute: "ROLE_ADMIN, ROLE_EDITOR"])

            addMapping([url: "/requestmap/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/user/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/role/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/address/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/dataTag/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/member/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/memberData/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/memberFlags/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/memberType/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/phone/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/donation/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/sponsorship/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/target/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/event/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/rsvp/**", configAttribute: "ROLE_ADMIN"])
            addMapping([url: "/rsvp/yes/*", configAttribute: "IS_AUTHENTICATED_REMEMBERED"])

            addMapping([url: "/member/join", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY"])

        }
    }

    void createMemberTypesIfRequired(){
        if(MemberType.count() == 0){
            save(new MemberType(name:"Full", description: "Full fee paying member"))
            save(new MemberType(name:"Senior", description: "Senior discount paying member"))
            save(new MemberType(name:"Student", description: "Student discount paying member"))
            save(new MemberType(name:"Associate", description: "Members family associate member"))
        }
    }

    void createMemberFlagsIfRequired(){
        if(MemberFlags.count() == 0){
            save(new MemberFlags(name:"Chairman", description: "Current chairman"))
            save(new MemberFlags(name:"Treasurer", description: "Current Treasurer"))
            save(new MemberFlags(name:"Webmaster", description: "Current Webmaster"))
            save(new MemberFlags(name:"Secretary", description: "Current Secretary"))
            save(new MemberFlags(name:"Volunteer", description: "Indicated wish to volunteer"))
            save(new MemberFlags(name:"Transport", description: "Has transport for events"))
            save(new MemberFlags(name:"Presenter", description: "Past presenter"))
        }
    }

    void createDataTagIfRequired(){
        if(DataTag.count() == 0){
            save(new DataTag(name:"Skill", description: "Skill indicated by member"))
            save(new DataTag(name:"Resource", description: "Resource available from member"))
            save(new DataTag(name:"Presentation", description: "Links to member presentation slides/resource"))
        }
    }

    void addMapping(params) {
        def requestmap = new Requestmap()
        requestmap.properties = params
        save(requestmap)
    }

    void save(item) {
        try {
            if (item.save(flush: true)) {
                println("$item created")
            } else {
                item.errors.allErrors.each { println it }
            }
        } catch (SQLException e) {
            log.debug "SQL exception while saving $e.message"
            while(ne = e.getNextException){
                log.debug ne.message
            }
        }
    }
} 