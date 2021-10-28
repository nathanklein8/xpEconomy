package com.gmail.neklein3.master;

import org.bukkit.Location;

public class TellerMachine {

    // TellerMachine is an object type that has two variables, location and enabled status.

    Location location;
    Boolean enabled;

    // used when creating a new instance of TellerMachine.  takes one input: the location of it
    public TellerMachine (Location location) {
        this.location = location;
    }

    // .setEnabled sets the boolean status of enabled
    public void setEnabled (Boolean bool) {
        enabled = bool;
    }

    // .getEnabled() returns true/false status of enabled
    public Boolean getEnabled() {
        return enabled;
    }

    // .getLocation() returns the location of the TellerMachine
    public Location getLocation() {
        return location;
    }

}