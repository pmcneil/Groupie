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

class Tag {

    String name

    String toString() {
        name
    }

    boolean equals(o) {
        log.debug "being compared with $o"
        if(this.is(o)) return true
        if(!(o instanceof Tag)) return false
        return this.name == o.name
    }

    int hashcode() {
        log.debug "hash called"
        return name.hashCode()
    }

    static constraints = {
        name(unique: true, maxSize: 255)
    }
}
