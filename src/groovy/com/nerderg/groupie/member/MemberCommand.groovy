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

class MemberCommand implements Serializable {

    String contactPhone
    String phoneType
    String phoneNote
    String address
    String state
    String country
    String postcode

    MemberCommand(){
    }

    MemberCommand(Member member){
        def address = member.memberAddress.toArray()[0] //may be better to do a find?
        def phone = member.memberPhone.toArray()[0]
        this.contactPhone = phone.number
        this.phoneType = phone.type
        this.phoneNote = phone.note
        this.address = address.address
        this.state = address.state
        this.country = address.country
        this.postcode = address.postcode
    }

    static memberTypes() {
        def types = MemberType.list().collect{it.name}
        return types
    }

    static constraints = {
        contactPhone(blank: false, maxSize: 20, minsize: 8, matches: /^([\d\s\+]*)$/)
        phoneType(inList: Phone.types)
        phoneNote(blank: true)
        address(blank: false)
        postcode(blank: false, minSize: 4)
        state(inList: Address.states)
        country(inList: Address.countries)
    }

}
