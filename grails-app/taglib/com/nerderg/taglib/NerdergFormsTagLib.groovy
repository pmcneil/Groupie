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

package com.nerderg.taglib

class NerdergFormsTagLib {
    static namespace = "nerderg"

    
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
        def label = attrs.remove("label").replaceAll('\\n','<br>')
        def delim = attrs.delim ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def div = attrs.table ? 'tr' : 'div'
        def span = attrs.table ? 'td' : 'span'
        def err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        out << """<$div class="prop">
          <$span class="name">
            <label for="$field">$label$delim</label>
          </$span>
          <$span class="value $err">"""
        out << body()
        out<< """</$span>
        </$div>"""
    }

    /*
     * attributes:
     *  field, label, bean
     */
    def inputfield = { attrs, body ->
        def label = attrs.remove("label").replaceAll('\\n','<br>')
        def delim = attrs.delim ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def type = attrs.remove("type")
        def div = attrs.table ? 'tr' : 'div'
        def span = attrs.table ? 'td' : 'span'

        def err = ""
        try {
            err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        } catch (e) {
            log.debug "errors error: $e.message"
        }
        def value = ""
        try {
            if(bean) {
                if (bean instanceof java.util.Map) {
                    value = bean[field] ? bean[field] : ""
                } else {
                    value = fieldValue(bean: bean, field: field)
                }
            }
        } catch (e) {
            log.debug "value error: $e.message"
        }

        if(!type) {
            type = (field == 'passwd' ? "password" : "text")
        }
        out << """<$div class="prop">
          <$span class="name">
            <label for="$field">$label$delim</label>
          </$span>
          <$span class="value $err">"""
        out << """<input type="$type" name="$field" value="$value" """
        if(!attrs.id) {
            out << """ id="$field" """
        }
        outputAttributes(attrs)
        out << "/>"
        out << body()
        out<< """</$span>
        </$div>"""
    }

    def datetimefield = { attrs, body ->
        def label = attrs.remove("label").replaceAll('\\n','<br>')
        def delim = attrs.delim ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def type = attrs.remove("type")
        def div = attrs.table ? 'tr' : 'div'
        def span = attrs.table ? 'td' : 'span'

        def err = ""
        try {
            err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        } catch (e) {
            log.debug "err: $e.message"
        }
        def date = ""
        def time = ""
        def value = ""
        try {
            if(bean) {
                if (bean instanceof java.util.Map) {
                    value = bean[field] ? bean[field] : ""
                } else {
                    value = fieldValue(bean: bean, field: field)
                }
                def valueSplit = value.split(" ")
                date = valueSplit[0]
                def timeSplit = valueSplit[1].split(':')
                time = timeSplit[0] + ':' +timeSplit[1] //convert to min:sec
            }
        } catch (e) {
            log.debug "value: $e.message"
        }

        out << """<$div class="prop">
          <$span class="name">
            <label for="$field">$label$delim</label>
          </$span>
          <$span class="value $err">"""
        out << """<input type="text" name="date$field" value="$date" class="date" size="10" """
        if(!attrs.id) {
            out << """ id="date-$field" """
        }
        outputAttributes(attrs)
        out << "/>&nbsp;"
        out << """<input type="text" name="time$field" value="$time" class="time" size="7" title="type or use mouse wheel to change" """
        if(!attrs.id) {
            out << """ id="time-$field" """
        }
        outputAttributes(attrs)
        out << "/>"
        out << """<input type="hidden" name="$field" value="$value" id="$field" />"""
        out << body()
        out<< """</$span>
        </$div>"""
    }

    def datefield = { attrs, body ->
        def label = attrs.remove("label").replaceAll('\\n','<br>')
        def delim = attrs.delim ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def type = attrs.remove("type")
        def div = attrs.table ? 'tr' : 'div'
        def span = attrs.table ? 'td' : 'span'

        def err = ""
        try {
            err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        } catch (e) {
            log.debug "err: $e.message"
        }
        def date = ""
        def value = ""
        try {
            if(bean) {
                if (bean instanceof java.util.Map) {
                    value = bean[field]
                } else {
                    value = fieldValue(bean: bean, field: field)
                }
                def valueSplit = value.split(" ")
                date = valueSplit[0]
            }
        } catch (e) {
            log.debug "value: $e.message"
        }

        out << """<$div class="prop">
          <$span class="name">
            <label for="$field">$label$delim</label>
          </$span>
          <$span class="value $err">"""
        out << """<input type="text" name="date$field" value="$date" class="date" size="10" """
        if(!attrs.id) {
            out << """ id="date-$field" """
        }
        outputAttributes(attrs)
        out << "/>&nbsp;"
        out << """<input type="hidden" name="$field" value="$value" id="$field" />"""
        out << body()
        out<< """</$span>
        </$div>"""
    }

    def checkboxgroup = { attrs, body ->
        def label = attrs.remove("label").replaceAll('\\n','<br>')
        def delim = attrs.delim ? attrs.delim : ":"
        def bean = attrs.remove("bean")
        def field = attrs.remove("field")
        def div = attrs.table ? 'tr' : 'div'
        def span = attrs.table ? 'td' : 'span'

        def err = bean ? hasErrors(bean: bean, field: field, 'errors') : ""
        def allValues = []
        allValues.addAll(attrs.remove("from"))
        def values = attrs.remove("subset")
        log.debug "lc:checkboxgroup values are $values"
        out << """<$div class="prop">
          <$span class="name">
            <label for="$field">$label$delim</label>
          </$span>
          <$span class="value $err">
            <ul class="cblist">"""
        allValues.each {val ->

            def checked = values?.contains(val)
            def value = val.hasProperty('id') ? val.id : val
            out << "<li><input type='checkbox' name='$field' value='$value' "
            if(checked) {
                out << "checked='checked' "
            }
            outputAttributes(attrs)
            out << ">&nbsp;$val</input></li>"
        }
        out << body()
        out<< """</ul>
            </$span>
        </$div>"""
    }

    def hiddenScriptData = {attrs, body ->
        out << "<script type='text/javascript'>\n"
        def items = attrs.items
        out << "var $attrs.keyPrefix = {"
        def comma = ''
        items?.each { item ->
            def key = item["$attrs.key"]
            def data = item["$attrs.data"].toString().encodeAsJavaScript()
            out << "$comma $key : \'$data\'\n"
            comma = ','
        }
        out << " };"
        out << "]\n</script>"
    }


    def asJSArray = {attrs ->
        out << "<script type='text/javascript'>\n"
        def items = attrs.items
        out << attrs.var.toString().encodeAsJavaScript() + " = ["
        def comma = ''
        def count = 0
        items?.each { item ->
            def data = item.toString().encodeAsJavaScript()
            out << "$comma\'$data\'"
            comma = ','
            if(count++ == 10){
                out << "\n"
                count = 0
            }
        }
        out << "]\n</script>"
    }

}
