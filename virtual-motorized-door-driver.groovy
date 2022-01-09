metadata {
    definition(name: 'Virtual Motorized Door', namespace: 'kvl', author: 'kevin.vanleer@gmail.com') {
        capability 'Garage Door Control'
        capability 'Door Control'
        capability 'Switch'

        command 'setVirtualDoorState'
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
    case 'closed':
      log.debug("opening")
      sendEvent(name: 'switch', value: 'on')
            break
    default:
    sendEvent(name: 'switch', value: 'on')
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
    case 'open':
            log.debug("closing")
            sendEvent(name: 'switch', value: 'on')
            break
    default:
    sendEvent(name: 'switch', value: 'on')
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

def update(newState) {
  log.debug("Called update")
  if(newState) {
    log.debug("update($newState)")
    state = [ *:state, *:newState ]
    log.debug("update::updated state: $state")
    sendEvent(name: 'door', value: state.door)
    sendEvent(name: 'switch', value: state.switch)
  }
}
