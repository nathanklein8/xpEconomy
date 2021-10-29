package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class TellerMachine {

    // TellerMachine is an object type that has two variables, location and enabled status.

    Block block;
    Boolean enabled;

    // used when creating a new instance of TellerMachine.  takes one input: the location of it
    public TellerMachine(Block block) {
        this.block = block;
    }

    // .getLocation() returns the location of the TellerMachine
    public Location getLocation() {
        return block.getLocation();
    }

    // .setEnabled sets the boolean status of enabled
    public void setEnabled(Boolean bool) {
        enabled = bool;
    }

    // .getEnabled() returns true/false status of enabled
    public Boolean getEnabled() {
        return enabled;
    }

    public String getStatus() {
        if (enabled) {
            return "" + ChatColor.GREEN + "Online!";
        } else {
            return "" + ChatColor.RED + "Offline!";
        }
    }

}