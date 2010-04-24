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

package com.nerderg.groupie.content

class FeedController {

    def contentService

    def index = {
        redirect action: 'rss'
    }

    def rss = {
        Content.withTransaction { status ->
            status.setRollbackOnly()
        }
        def cntx = "http://" + request.getHeader("Host") + request.getContextPath()
        log.debug "Context path is $cntx"
        def tag = params.id && params.id != 'index' ? params.id : "news"
        def testTag = Tag.findByName(tag)
        if(testTag) {
            log.debug "Feed for tag $tag"
            render(feedType:"rss", feedVersion:"2.0") {
                def tagPage = contentService.getTaggedItems(tag, 'page', 1)[0]
                title = tagPage ? tagPage.current.title : "Groupie feed"
                log.debug "title is $title"
                link = g.createLink(controller: 'display', action: 'index', base: "$cntx")
                description = "$title feed"

                //get the latest news items
                contentService.getTaggedItems(tag, 'content', 10).each() { article ->
                    entry(article.current.title) {
                        //todo link to articles page
                        link = g.createLink(controller: 'display', action: 'index', params: ['name': tagPage ? tagPage.name : 'home'], base: "$cntx")
                        article.current.content // return the content ***todo process the content ***
                    }
                }
            }
        } else {
            render(feedType:"rss", feedVersion:"2.0") {
                title = "$tag doesn't exist. Empty feed"
                log.debug "Blank feed since tag $tag doesn't exist"
                link = g.createLink(controller: 'display', action: 'index', base: "$cntx")
                description = "$tag doesn't exist. Empty feed"
            }
        }
    }

    def atom = {
        Content.withTransaction { status ->
            status.setRollbackOnly()
        }
        def cntx = "http://" + request.getHeader("Host") + request.getContextPath()
        def tag = params.id && params.id != 'index' ? params.id : "news"
        def testTag = Tag.findByName(tag)
        if(testTag) {
            log.debug "Feed for tag $tag"
            render(feedType:"atom", feedVersion:"1.0") {
                def tagPage = contentService.getTaggedItems(tag, 'page', 1)[0]
                title = tagPage ? tagPage.current.title : "Groupie feed"
                log.debug "title is $title"
                link = g.createLink(controller: 'display', action: 'index', params: ['name': tagPage?.name], base: "$cntx")
                description = "$title feed"

                //get the latest news items
                contentService.getTaggedItems(tag, 'content', 10).each() { article ->
                    entry(article.current.title) {
                        //todo link to articles page
                        link = g.createLink(controller: 'display', action: 'index', params: ['name': tagPage ? tagPage.name : 'home'], base: "$cntx")
                        article.current.content // return the content ***todo process the content ***
                    }
                }
            }
        } else {
            render(feedType:"atom", feedVersion:"1.0") {
                title = "$tag doesn't exist. Empty feed"
                log.debug "Blank feed since tag $tag doesn't exist"
                link = g.createLink(controller: 'display', action: 'index', base: "$cntx")
                description = "$tag doesn't exist. Empty feed"
            }
        }
    }
}
