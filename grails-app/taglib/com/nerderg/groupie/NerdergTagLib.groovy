/*
Copyright 2009, 2010 Peter McNeil

This file is part of Groupie.

Groupie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Groupie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Groupie.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nerderg.groupie
import com.nerderg.FlowService
//import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib
import org.springframework.web.servlet.support.RequestContextUtils as RCU;

class NerdergTagLib {
    static namespace = "nerderg"

    def flowService
    
    void outputAttributes(attrs)
    {
        attrs.remove('tagName') // Just in case one is left
        def writer = getOut()
        attrs.each {k, v ->
            writer << "$k=\"${v.encodeAsHTML()}\" "
        }
    }

    /*
     * attributes:
     *  field, label, bean
     */
    def formfield = { attrs, body ->
        def label = attrs.remove("label")
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        out << """<div class="prop">
          <span class="name">
            <label for="$field">$label:</label>
          </span>
          <span class="value $err">"""
        out << body()
        out<< """</span>
        </div>"""
    }

    /*
     * attributes:
     *  field, label, bean
     */
    def inputfield = { attrs, body ->
        def label = attrs.remove("label")
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def type = attrs.remove("type")

        def err = ""
        try {
            err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        } catch (e) {
            log.debug "err: $e.message"
        }
        def value = ""
        try {
            value = bean ? fieldValue(bean: bean, field: field) : ""
        } catch (e) {
            log.debug "value: $e.message"
        }

        if(!type) {
            type = (field == 'passwd' ? "password" : "text")
        }
        out << """<div class="prop">
          <span class="name">
            <label for="$field">$label:</label>
          </span>
          <span class="value $err">"""
        out << """<input type="$type" name="$field" value="$value" """
        if(!attrs.id) {
            out << """ id="$field" """
        }
        outputAttributes(attrs)
        out << "/>"
        out << body()
        out<< """</span>
        </div>"""
    }


    def datetimefield = { attrs, body ->
        def label = attrs.remove("label")
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def type = attrs.remove("type")

        def err = ""
        try {
            err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        } catch (e) {
            log.debug "err: $e.message"
        }
        def date = ""
        def time = ""
        try {
            if(bean) {
                def value = fieldValue(bean: bean, field: field).split(" ")
                date = value[0]
                time = value[1]
            }
        } catch (e) {
            log.debug "value: $e.message"
        }

        out << """<div class="prop">
          <span class="name">
            <label for="$field">$label:</label>
          </span>
          <span class="value $err">"""
        out << """<input type="text" name="date$field" value="$date" class="date" size="10" """
        if(!attrs.id) {
            out << """ id="date-$field" """
        }
        outputAttributes(attrs)
        out << "/>&nbsp;"
        out << """<input type="text" name="time$field" value="$time" class="time" size="7" """
        if(!attrs.id) {
            out << """ id="time-$field" """
        }
        outputAttributes(attrs)
        out << "/>"
        out << body()
        out<< """</span>
        </div>"""
    }

    def checkboxgroup = { attrs, body ->
        def label = attrs.remove("label")
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        def allValues = []
        allValues.addAll(attrs.remove("from"))
        def values = attrs.remove("subset")
        out << """<div class="prop">
          <span class="name">
            <label for="$field">$label:</label>
          </span>
          <span class="value $err">
          <form>
            <ul class="cblist">"""
        allValues.each {val ->

            def checked = values.contains(val)

            out << "<li><input type='checkbox' name='$field' value='$val.id' "
            if(checked) {
                out << "checked='checked' "
            }
            outputAttributes(attrs)
            out << ">&nbsp;$val</input></li>"
        }
        out << body()
        out<< """</ul>
            </form>
            </span>
        </div>"""
    }

    def hiddenScriptData = {attrs, body ->
        def mkp = new groovy.xml.MarkupBuilder(out)
        mkp.script('type': 'text/javascript') {
            def items = attrs.items
            yieldUnescaped "var $attrs.keyPrefix = {"
            items?.each { item ->
                def key = item["$attrs.key"]
                def data = item["$attrs.data"].toString().encodeAsJavaScript()
                yieldUnescaped "$key : \'$data\', \n"
            }
            yieldUnescaped " dummy : ''};"
        }
    }

    def cleanflowscope = {attrs ->
        flowService.cleanUpSession(attrs.flow)
    }

    def logit = { attrs ->
        log.debug attrs.message
    }

}