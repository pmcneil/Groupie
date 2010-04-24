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

class Item {

    String type
    String name
    Content current
    Map params  // string map of params for this item
    String readPerms
    String writePerms

    static belongsTo = [parent : Item]
    static hasMany = [content: Content, tags: Tag, children: Item]

    static constraints = {
        name(blank: false, unique: 'type', maxSize: 255)
        type(inList: ['template', 'page', 'menu', 'style', 'module', 'script', 'header', 'footer', 'sidebar', 'content', 'media', 'macro'])
        current(nullable: true)
        parent(nullable: true)
        readPerms(nullable: true, inList: ['Anyone', 'Registered Users', 'Author', 'Editors', 'Admin Only'])
        writePerms(nullable: true, inList: ['Author', 'Editors', 'Admin Only'])
    }

    def Item(){
        super()
    }
    def Item(Item item){
        super()

        type = item.type
        name = "$item.name (copy)"
        current = item.current
        readPerms = item.readPerms
        writePerms = item.writePerms
    }
}
