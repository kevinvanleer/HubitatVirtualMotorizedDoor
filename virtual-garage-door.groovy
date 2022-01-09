/**
 *  Virtual Garage Door Manager
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
 *  Author: Patrick McKinnon (patrick@ojolabs.com)
 */

definition(
  name: 'Virtual Garage Door',
  namespace: 'peng1can',
  author: 'peng1can@gmail.com',
  description: "Manages state of 'Virtual Garage Door' device (Child App)",
  category: 'Convenience',
  parent: 'peng1can/parent:Virtual Garage Door Manager',
  iconUrl: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png',
  iconX2Url: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png'
)

preferences {

    section('Garage door closed sensor') {
        input 'closedSensor', 'capability.switch',
    title: 'Garage Door Closed Sensor',
    required: true
    }

    section('Garage door open sensor') {
        input 'openSensor', 'capability.switch',
    title: 'Garage Door Open Sensor',
    required: true
    }
    section('Garage door actuator') {
        input 'actuatorSwitch', 'capability.switch',
    title: 'Garage Door Actuator Switch',
    required: true
    }
    section('Virtual garage door device') {
        input 'garage', 'capability.switch',
    title: 'Virtual Garage Door',
    required: true
    }

}

def installed() {
    log.debug ("Virtual garage door installed")
    initialize()
}

def updated() {
    log.debug ("Virtual garage door updated")
    unsubscribe()
    initialize()
    log.debug("update done")
}

private initialize() {
  log.debug("initialize")
    state = ['door':'closed','switch':'off'] //causes null pointer exception
    //state = [:]
    if (garage.supportedCommands.find { it.name == 'setVirtualGarageState' }) {
        subscribe(actuatorSwitch, 'switch', isActuatedHandler)
        subscribe(openSensor, 'switch', isOpenHandler)
        subscribe(closedSensor, 'switch', isClosedHandler)
        subscribe(garage, 'door', garageControlHandler)
        subscribe(garage, 'switch', garageSwitchHandler)

        //not receiving garage switch events
        
        log.debug(garage.capabilities)

        state.door = closedSensor.currentSwitch == 'on' ? 'closed' : openSensor.currentSwitch == 'on' ? 'open' : 'unknown'
        state.switch = actuatorSwitch.currentSwitch
        log.debug(state)

        synchronize()
        log.debug("init done")
}
  else {
        log.error("Virtual Garage Door device should by of type pmckinnon/'Virtual Garage Door'")
  }
}

private synchronize() {
    log.debug "synchronize, current: $state"

    garage.setVirtualGarageState([
  door: state.door,
        switch: state.switch
  ])
    log.debug("sync done")
}

def isOpenHandler(evt) {
    log.debug("isOpenHandler: $evt")
    state.door = (evt.value == 'on') ? 'open' : 'closing'
    onSensorChanged()
}

def isClosedHandler(evt) {
    log.debug("isClosedHandler: $evt")
    state.door = (evt.value == 'on') ? 'closed' : 'opening'
    onSensorChanged()
}

private onSensorChanged() {
    log.debug("onSensorChanged(): $state")
    synchronize()
}

def garageControlHandler(evt) {
    log.debug "garageControlHandler($evt.value)"
    switch (evt.value) {
      case 'open':
      case 'close':
            //triggerActuator()
            break      
      default:
      log.debug('unknown door event')
    }
}

def garageSwitchHandler(evt) {
    log.debug "garageSwitchHandler($evt.value), current: ${state.switch}"

    if (evt.value == 'on') {
        log.debug('Triggering door action')
        triggerActuator()
        state.switch = evt.value
  } else {
        log.debug('Actuator reset')
    }
}

def isActuatedHandler(evt) {
    log.debug("got actuator switch event: ${evt}")
        state.switch = evt.value
        synchronize()
}

private triggerActuator() {
    log.debug 'triggerActuator()'
    actuatorSwitch.on()
}