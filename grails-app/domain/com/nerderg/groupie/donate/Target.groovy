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

package com.nerderg.groupie.donate

class Target implements Serializable {

    String name
    Date dateSubscribed
    boolean fullySubscribed = false

    public String toString(){
        "$id - $name"
    }
    static hasMany = [donations: Donation]

    static constraints = {
        name(unique: true, maxSize: 255)
        dateSubscribed(nullable: true)
    }
}
