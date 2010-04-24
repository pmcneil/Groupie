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

security {

    // see DefaultSecurityConfig.groovy for all settable/overridable properties

    active = true
    cacheUsers = false

    loginUserDomainClass = "com.nerderg.groupie.GUser"
    authorityDomainClass = "com.nerderg.groupie.Role"
    requestMapClass = "com.nerderg.groupie.Requestmap"

    useOpenId = false
    useRequestMapDomainClass = true

//    requestMapString = """\
//CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
//PATTERN_TYPE_APACHE_ANT
///=IS_AUTHENTICATED_ANONYMOUSLY
///**=IS_AUTHENTICATED_ANONYMOUSLY
//"""
}
