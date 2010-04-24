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

package com.nerderg.groupie.karunya

import com.nerderg.groupie.donate.*
import com.nerderg.groupie.content.*

class SponsorService {

    boolean transactional = true

    transient updateTargetsFromTaggedContent() {
        log.debug "updating targets"
        def sponsorItemNames = []
        def tag = Tag.findByName('Sponsor Me')
        transient items = Item.findAllByType('content',[fetch: [tags: 'eager']])
        items.each{item ->
            item.tags.each {
                if(it == tag) {
                    sponsorItemNames.add(new String(item.name))
                }
            }
        }
        
        sponsorItemNames.each {name ->
            findOrCreateTarget(name)
        }
    }

    def findSponsors(target) {
        return Sponsorship.findAllByTarget(target)
    }

    def findOrCreateTarget(name) {
        def target = Target.findByName(name)
        if(!target){
            target = new Target(name: name)
            save(target)
        }
        return target
    }

    def sponsor(member, targetCommand, amount){
        log.debug "member $member target $targetCommand"
        def target = Target.get(targetCommand.id)
        def sponsorship = new Sponsorship()
        sponsorship.startDate = new Date()
        sponsorship.member = member
        sponsorship.target = target
        sponsorship.frequency = 'monthly'
        sponsorship.amount = amount
        if(save(sponsorship)){
            return sponsorship
        }
        return null;
    }


    def save(item){
        save(item, false)
    }

    def save(item, flsh) {
        if (item.save(flush: flsh)) {
            if(item.hasErrors()){
                log.debug "*** Errors Saving ***"
                item.errors.allErrors.each { log.debug it }
                return false
            }
            log.debug("$item saved")
            return true
        } else {
            log.debug "*** Errors Saving ***"
            item.errors.allErrors.each { log.debug it }
            return false
        }
    }

}
