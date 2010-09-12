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

package com.nerderg.groupie

class PerfTagLib {
    static namespace = "p"

    void outputAttributes(attrs)
    {
        attrs.remove('tagName') // Just in case one is left
        def writer = getOut()
        attrs.each {k, v ->
            writer << "$k=\"${v.encodeAsHTML()}\" "
        }
    }

    def css = { attrs ->
        def url = g.resource(dir:'css', file: attrs.name + ".css")
        out << """<link rel='stylesheet' type='text/css' href='$url' />"""
    }

    def javascript = { attrs ->
        def url = g.resource(dir:'js', file: attrs.src + ".js")
        out << """<script src='$url'></script>"""
    }

    def image = { attrs ->
        def url = g.resource(dir:'images', file: attrs.remove('src'))
        out << "<img src='$url' "
        outputAttributes(attrs)
        out << "/>"
    }
}
