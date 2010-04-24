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

class MimeService {

    boolean transactional = true

    static final appTypeMatch =[
        'ogg' : 'audio',
        'x-ms-wma' : 'audio',
        'pdf' : 'document',
        'vnd.oasis.opendocument.text' : 'document',
        'vnd.oasis.opendocument.spreadsheet' : 'document',
        'vnd.oasis.opendocument.presentation' : 'document',
        'vnd.ms-powerpoint' : 'document',
        'msword' : 'document'
    ]

    /**
     * This produces an extended major type for mime types by adding 'document'
     * It also looks at application/* and filters out major application files to
     * the common major type, e.g. application/ogg is mostly used for audio even
     * though audio/ogg exists
     *
     * Returns other, image, audio, video, document
     *
     */
    def duckTyper(mimetype) {
        def mtype = mimetype.split('/')
        if(mimetype.startsWith("application")){
            def guess = appTypeMatch[mtype[1]]
            return guess ? guess : 'other'
        }
        else {
            return mtype[0]
        }

    }
}
