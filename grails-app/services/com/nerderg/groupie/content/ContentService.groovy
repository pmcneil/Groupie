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

import com.nerderg.groupie.GUser
import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.web.pages.*
import groovy.text.Template
import org.htmlcleaner.*

class ContentService {

    boolean transactional = true
    def GroovyPagesTemplateEngine groovyPagesTemplateEngine

    static final mimeMatch = ['template': 'text/html',
                     'page': 'text/html',
                     'menu': 'text/html',
                     'style': 'text/css',
                     'module': 'text/html',
                     'macro': 'text/html',
                     'script':'text/javascript',
                     'header': 'text/html',
                     'footer': 'text/html',
                     'sidebar': 'text/html',
                     'content': 'text/html',
                     'media': 'text/html']

    def rnd = new Random();

    /**
     * find the current content. If the current content doesn't exist then create it
     */
    def getOrCreateContent(String name, String type, GUser author) {
        log.debug "get or createContent: $name, $type"
        def item = Item.findByNameAndType(name, type)
        if(!item) {
            item = new Item(name: name, type: type)
            if(save(item)) {
                def content = createContent(item, author)
                if(!content) {
                    log.debug "createContent didn't seem to work"
                }
                item.current = content
                save(item, true)
                return content
            } else {
                log.debug "item save didn't work"
                return item;
            }
        }
        if(item.current){
            return item.current
        }
        //hmm have item but no current content
        log.debug "item exists but no content"
        def content = createContent(item, author)
        item.current = content
        save(item, true)
        return content
    }


    Content createContent(String name, String type, GUser author) {
        log.debug "createContent 1"
        def item = Item.findByNameAndType(name, type)
        if(!item) {
            item = new Item(name: name, type: type)
            if(save(item)) {
                return createContent(item, author)
            } else {
                throw new ContentException("Can't create content $name or type $type")
            }
        }
        return createContent(item, author)
    }

    Content createContent(Item item, GUser author) {
        log.debug "createContent 2"
        def content = new Content()
        content.title = item.name
        content.mimeType = mimeMatch[item.type]
        content.item = item
        content.author = author
        content.content = makeDefaultContent(item)
        if(save(content)) {
            return content
        }
        throw new ContentException("Can't create content for item $item.id - $item.name of type $item.type")
    }

    Content createContent(Item item, GUser author, Content content) {
        log.debug "createContent 2"
        def clone = new Content()
        clone.title = content.title
        clone.mimeType = content.mimeType
        clone.item = item
        clone.author = author
        clone.content = content.content
        if(save(clone)) {
            return clone
        }
        throw new ContentException("Can't create content for item $item.id - $item.name of type $item.type")
    }

    String makeDefaultContent(item){
        switch(item.type){
            case 'style':
            return "/*edit $item.name styles*/"
            break
            case 'script':
            return "//edit $item.name script"
            break
            default:
            return "<h1>$item.name</h1>"
        }
    }

    HtmlCleaner getCleaner() {
        HtmlCleaner cleaner = new HtmlCleaner()
        CleanerProperties props = cleaner.getProperties()
        props.setAllowHtmlInsideAttributes(true)
        props.setRecognizeUnicodeChars(true)
        props.setUseEmptyElementTags(false)
        props.setAdvancedXmlEscape(true)
        props.setTranslateSpecialEntities(true)
        props.setEscapeAttributes(false)
        props.setTreatUnknownTagsAsContent(false)
        return cleaner
    }

    String filterContent(String text){
        log.debug "filter content"

        HtmlCleaner cleaner = getCleaner()
        def node = cleaner.clean(text)
        def body = node.findElementByName("body", false)
        def divs = body.getElementListByName("div", true).grep {
            def id = it.getAttributeByName('id')
            id?.contains('item-') || id?.contains('media-')
        }
        divs.each{
            cleaner.setInnerHtml(it, '')
        }
        def imgs = body.getElementListByName("img", true).grep {
            it.getAttributeByName('src')?.contains('/show/')
        }
        imgs.each{
            log.debug "image filter $it"
            //they could use width height attribs or style="width: 402px; height: 301px;"
            def width = it.getAttributeByName('width')
            def height = it.getAttributeByName('height')
            if(!(width && height)){
                def style = it.getAttributeByName('style')
                log.debug "image filter style: $style"
                if(style){
                    def m = style =~ /width:\s*(\d*)px;\s*height:\s*(\d*)/
                    if(m){
                        width = m[0][1]
                        height = m[0][2]
                    }
                }
            } else {
                it.removeAttribute('width')
                it.removeAttribute('height')
            }
            log.debug "image filter width: $width, height: $height"
            if(width && height){
                def src = it.getAttributeByName('src')
                src = src.substring(0, src.indexOf("?"))
                log.debug "image filter src: $src"
                it.removeAttribute('src')
                it.addAttribute('src', "$src?w=$width&h=$height")
            }
        }

        def newText = cleaner.getInnerHtml(body).trim()
        newText.replaceAll(/\$/, "&#36;")
    }

    String embedContent(String text) {
        
        log.debug "embedContent"
        HtmlCleaner cleaner = getCleaner()
        def node = cleaner.clean(text)
        def body = node.findElementByName("body", false)
        def divs = body.getElementListByName("div", true).grep {
            it.getAttributeByName('id')?.contains('item-')
        }
        divs.each{ div ->
            def idTxt = div.getAttributeByName('id')
            def itClass = div.getAttributeByName('class')
            println "class is $itClass"
            if(!itClass.contains('embeded')) {
                itClass += ' embeded'
                div.attributes.'class' = itClass
            }
            def item = Item.get(getItemIdFromEditableDivId(idTxt))
            if(item) {
                cleaner.setInnerHtml(div, item.current.content)
                div.addAttribute('title', item.current.title)
            } else {
                cleaner.setInnerHtml(div, "$idTxt not found.")
            }
        }
        def medias = body.getElementListByName("div", true).grep {
            it.getAttributeByName('id')?.contains('media-')
        }
        medias.each{ div ->
            def idTxt = div.getAttributeByName('id')
            def itClass = div.getAttributeByName('class')
            println "class is $itClass"
            if(!itClass?.contains('embeded')) {
                itClass += ' embeded'
                div.attributes.'class' = itClass
            }
            def media = Media.get(getMediaIdFromDivId(idTxt))
            if(media) {
                log.debug "media is $media <groupie:media mid='$media.id'/>"
                cleaner.setInnerHtml(div, "<groupie:media mid='$media.id'/>")
            } else {
                cleaner.setInnerHtml(div, "$idTxt not found.")
            }
        }
        def ec = cleaner.getInnerHtml(body)
        return ec.replaceAll(/(<\!\[CDATA\[|\]\]>)/, '')
    }

    def getItemIdFromEditableDivId(divId) {
        divId.replaceAll(/item-(\d+)/, '$1')
    }

    def getMediaIdFromDivId(divId) {
        log.debug "media Id text is $divId"
        divId.replaceAll(/media-(\d+)/, '$1')
    }

    boolean testContent(content) {
        if(content != null) {
            try{
                Template t = groovyPagesTemplateEngine.createTemplate(content, "test.gsp")
                t.make(content: content)
                return true
            } catch (RuntimeException e) {
                println e.message
                return false
            }
        }
    }

    def processGSPContent(model, name, out) {
        log.debug("model is $model")
        Template t = groovyPagesTemplateEngine.createTemplate(model.content.content, name + ".gsp")
        Writable w = t.make(model: model)
        w.writeTo(out)
    }

    def save(item){
        save(item, false)
    }

    def save(item, flsh) {
        if (item.save(flush: flsh)) {
            if(item.hasErrors()){
                log.debug "*** Errors Saving ***"
                item.errors.allErrors.each { log.debug it }
                return false
            }
            log.debug("$item saved")
            return true
        } else {
            log.debug "*** Errors Saving ***"
            item.errors.allErrors.each { log.debug it }
            return false
        }
    }

    def getTaggedContent(tagName, limit) {
        return getTaggedItems(tagName, 'content', limit)
    }

    def getTaggedItems(tagName, type, limit) {
        return Item.createCriteria().list() {
            and {
                eq('type', type)
                if(tagName) {
                    tags {
                        eq('name', tagName)
                    }
                }
            }
            if(limit != null){
                maxResults(limit.toInteger())
            }
            order("id", "desc")
        }
    }

}
