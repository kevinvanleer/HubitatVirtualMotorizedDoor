/**
 *  Virtual Garage Door
 *
 *  Copyright 2017 Patrick McKinnon
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Original Author: Patrick McKinnon (patrick@ojolabs.com)
 *  Hubitat patches from @stephack, @peng1can
 */
metadata {
    definition (name: 'Virtual Garage Door', namespace: 'peng1can', author: 'peng1can@gmail.com') {
        capability 'Garage Door Control'
        capability 'Switch'

        command 'setVirtualGarageState'
    }

    simulator {
    }
}

def open() {
    log.debug 'open()'
    log.debug(state)

    switch (state['door']) {
    case 'open':
            log.debug('The door is already open')
            break
    case 'opening':
            log.debug('The door is already opening')
            break
    case 'closing':
            sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'open')
    case 'closed':
      log.debug("opening")
      sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'open')
            break
    default:
    sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'open')
    }
}

def close() {
    log.debug 'close()'
    switch (state['door']) {
    case 'closed':
            log.debug('The door is already closed')
            break
    case 'closing':
            log.debug('The door is already closing')
            break
    case 'opening':
log.debug("stopping")
            sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'close')
    case 'open':
            log.debug("closing")
            sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'close')
            break
    default:
    sendEvent(name: 'switch', value: 'on')
      //sendEvent(name: 'door', value: 'close')
    }
}

def on() {
    log.debug('VGD on()')
    log.debug(state)
    switch (state['door']) {
    case 'open':
    case 'opening':
            close()
            break
    case 'closed':
    case 'closing':
            open()
            break
    default:
    sendEvent(name: 'switch', value: 'on')
    }
}

def off() {
    log.debug('VGD off()')
}

def setVirtualGarageState(newState) {
  log.debug("Called setVirtualGarageState")
  if(newState) {
    log.debug("setVirtualGarageState($newState)")
    state = [ *:state, *:newState ]
    log.debug("setVirtualGarageState::updated state: $state")
    sendEvent(name: 'door', value: state['door'])
    sendEvent(name: 'switch', value: state['switch'])
  }
}
