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

package com.nerderg

class FlowService {

    boolean transactional = true

    /**
     * Remove non-serializable entities from the flows persistenceContext from
     * listing or iterating through domain objects
     * 
     */
    def cleanUpSession(flow) {
        def remove = []
        if (flow && flow.persistenceContext){
            flow.persistenceContext.getPersistenceContext().getEntitiesByKey().values().each { entity ->
                if(!entity instanceof Serializable){
                    remove.add(entity)
                }
            }
            remove.each {flow.persistenceContext.evict(it)}
        }
    }
}
