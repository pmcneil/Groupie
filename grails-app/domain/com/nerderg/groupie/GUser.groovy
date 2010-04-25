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

import com.nerderg.groupie.Role
import com.nerderg.groupie.member.Member
import com.nerderg.groupie.mail.MailingList

/**
 * User domain class.
 */
class GUser implements Serializable {
    static transients = ['pass']
    static hasMany = [authorities: Role, mailingLists: MailingList]
    static belongsTo = Role

    /** Username */
    String username
	
    /** User Real Name*/
    String salutation
    String firstName
    String lastName

    /** MD5 Password */
    String passwd
    /** enabled */
    boolean enabled

    String email
    boolean emailShow

    /** description */
    String description = ''

    /** plain password to create a MD5 password */
    String pass = '[secret]'

    Member member

    String toName(){
        return "$firstName $lastName"
    }

    String toString(){
        return "$username - $salutation $firstName $lastName"
    }

    static mapping = {
        member lazy:false
    }

    static constraints = {
        username(blank: false, unique: true, maxSize: 100)
        salutation(nullable: true, maxSize: 20)
        firstName(blank: false, maxSize: 100)
        lastName(blank: false, maxSize: 100)
        passwd(blank: false, maxSize: 100)
        email(nullable: true, email: true, maxSize: 100)
        member(nullable: true)
    }

}
