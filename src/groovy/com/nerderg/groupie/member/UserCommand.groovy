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

package com.nerderg.groupie.member

import com.nerderg.groupie.GUser

class UserCommand implements Serializable {
    String username
    String salutation
    String firstName
    String lastName
    String passwd
    String repasswd
    String email

    UserCommand(){
    }

    UserCommand(user){
        this.username = user.username
        this.salutation = user.salutation
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.passwd = user.passwd
        this.email = user.email
    }
    static constraints = {
        username(blank: false, validator: {username ->
                def u = GUser.findByUsername(username)
                return u == null
            })
        salutation(blank: false)
        firstName(blank: false)
        lastName(blank: false)
        passwd(blank: false)
        email(email: true)
    }
}