# Motorized Door

Driver and app for interacting with a motorized door using the Hubitat Elevation Smart Home Hub. The virtual devices uses three switches to control and monitor the door. The switches should be wired as follows:

* Activator: this switch should cause the door change state when closed
* Open sensor: this switch should be closed when the door is in the open state
* Close sensor: this switch should be closed when the door is in the closed state

Originally designed and developed for a Genie Pro Screw Driver garage door from the the early 2000s integrated with a Zooz MultiRelay ZEN16, but should be able to integrate with any three switches used to operate a motorized door.

This software was inspired by "Virtual Garage Door" published by Patrick McKinnon in 2017 and ported to Hubitat Elevation by D Canfield and served as the basis for the implementation:
https://community.hubitat.com/t/release-virtual-garage-door-app-driver-st-port/15668. Without this code it would have been much more difficult to figure out the hubitat driver and application interfaces. It is the position of the author that the changes here are significant enough to call this different software.

# Developer notes

The learning curve for Hubitat Elevation apps and drivers is fairly steep, given the lack of documentation. One important note is that the programming interface seems to have been derived from SmartThings as there seem to be many apps and drivers ported from SmartThings. However, not all SmartThings (ST) features are supported by Hubitat Elevation (HE). Of these, "tiles" is the most notable unsupported features. HE does not allow users to define custom dashboard tiles, and must conform to the "capabilities" of the standard tiles. https://docs.hubitat.com/index.php?title=Driver_Capability_List

Motorized door consists of three parts, this design is from the original "Virtual Garage Door" published by Patrick McKinnon. The driver interacts with the three switches, monitoring the state of the door position sensors and activating the door motor switch. The app integrates the three switches into a single garage door control state model that can be controlled with a "Garage Door (Control)" tile. The manager app organizes instantiations of motorized door.

# LICENSE

Copyright 2022 Kevin Van Leer <kevin.vanleer@gmail.com> 
kevinvanleer.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
