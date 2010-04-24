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

class UrlMappings {
    static mappings = {
      "/$controller/$action/$id?"{
            constraints {
                // apply constraints here
            }
        }
      "/robots.txt" (view: "/robots")
      "/login" (controller: "login", action: "auth")
      "/admin" (view: "/index")
      "/sponsor" (controller: "sponsor", action: "index")
      "/tagged/$tag" (controller: "display", action: "tagged")
      "/$name" (controller: "display", action: "index")
      "/" (controller: "display", action: "index")
      "404"(view:'/notfound')
      "500"(view:'/error')
    }
}
