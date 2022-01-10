definition(
    name: 'Virtual Motorized Door',
    namespace: 'kvl',
    author: 'kevin.vanleer@gmail.com',
    description: 'A virtual device for activating and monitoring any motorized door.\
                The door should be activated by a monentary\
                switch and use momentary switches to monitor open and closed state.',
    category: 'Convenience',
    parent: 'kvl:Virtual Motorized Door Manager',
    iconUrl: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png',
    iconX2Url: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png'
)

preferences {
    section('Motorized door closed sensor') {
        input 'closedSensor', 'capability.switch',
        title: 'Door Closed Sensor',
        required: true
    }
    section('Motorized door open sensor') {
        input 'openSensor', 'capability.switch',
        title: 'Door Open Sensor',
        required: true
    }
    section('Motorized door door motor switch') {
        input 'doorMotorSwitch', 'capability.switch',
        title: 'Door Motor Switch',
        required: true
    }
    section('Virtual motorized door device') {
        input 'motorizedDoor', 'capability.garageDoorControl',
        title: 'Virtual Device (type: Virtual Motorized Door)',
        required: true
    }
}

def installed() {
    log.debug('Virtual motorized door installed')
    initialize()
}

def updated() {
    log.debug('Virtual motorized door updated')
    unsubscribe()
    initialize()
    log.debug('update done')
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

def motorizedDoorControlHandler(evt) {
    log.debug "motorizedDoorControlHandler($evt.value)"
    switch (evt.value) {
        case 'open':
            log.debug('door is open')
            break
        case 'closed':
            log.debug('door is closed')
            break
        case 'opening':
            log.debug('door is opening (inferred)')
            break
        case 'closing':
            log.debug('door is closing (inferred)')
            break
        default:
            log.debug('unknown door event')
    }
}

def motorizedDoorSwitchHandler(evt) {
    log.debug "motorizedDoorSwitchHandler($evt.value), current: ${state.switch}"

    if (evt.value == 'on') {
        log.debug('Triggering door action')
        activateDoorMotor()
    } else {
        log.debug('Door motor reset')
    }
}

def isActivatedHandler(evt) {
    log.debug("got door motor switch event: ${evt}")
    state.switch = evt.value
    synchronize()
}

private onSensorChanged() {
    log.debug("onSensorChanged(): $state")
    synchronize()
}

private activateDoorMotor() {
    log.debug 'activateDoorMotor()'
    doorMotorSwitch.on()
}

private synchronize() {
    log.debug "synchronize, current: $state"

    motorizedDoor.update([door: state.door, switch: state.switch])
    log.debug('sync done')
}

private initialize() {
    log.debug('initialize')
    state = ['door':'closed', 'switch':'off'] //causes (harmless?) null pointer exception
    if (motorizedDoor.supportedCommands.find { command -> command.name == 'update' }) {
        subscribe(doorMotorSwitch, 'switch', isActivatedHandler)
        subscribe(openSensor, 'switch', isOpenHandler)
        subscribe(closedSensor, 'switch', isClosedHandler)
        subscribe(motorizedDoor, 'door', motorizedDoorControlHandler)
        subscribe(motorizedDoor, 'switch', motorizedDoorSwitchHandler)

        log.debug(motorizedDoor.capabilities)

        state.door = closedSensor.currentSwitch == 'on' ? 'closed'
            : openSensor.currentSwitch == 'on' ? 'open' : 'unknown'
        state.switch = doorMotorSwitch.currentSwitch
        log.debug(state)

        synchronize()
        log.debug('init done')
    } else {
        log.error("Virtual Motorized Door device should by of type kvl/'Virtual Motorized Door'")
    }
}
