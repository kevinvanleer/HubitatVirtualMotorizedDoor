definition(
  name: 'Virtual Motorized Door Manager',
  namespace: 'kvl',
  author: 'kevin.vanleer@gmail.com',
  description: "Manages Instances of 'Virtual Motorized Door'",
  category: 'Convenience',
  singleInstance: true,
  iconUrl: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png',
  iconX2Url: 'https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png'
)

preferences {
    page(name: 'mainPage', title: 'Motorized Doors', install: true, uninstall: true) {
        section {
            app(name: 'virtualMotorizedDoor',
      appName: 'Virtual Motorized Door',
      namespace: 'kvl',
      title: 'New Door',
      multiple: true)
        }
    }
}
