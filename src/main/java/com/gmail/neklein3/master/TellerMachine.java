package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("TellerMachine")
public class TellerMachine implements ConfigurationSerializable {

    // TellerMachine is an object type that has two variables, location and enabled status.

    Location location;
    Boolean enabled;
    Boolean twoWayMode;

    // used when creating a new instance of TellerMachine.  takes one input: the location of it
    public TellerMachine(Location l) {
        location = l;
        enabled = true;
        twoWayMode = false;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Location", location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        result.put("Enabled", enabled);
        result.put("TwoWayMode", twoWayMode);
        return result;
    }

    public static TellerMachine deserialize(Map<String, Object> map) {
        Boolean enabled = (Boolean) map.get("Enabled");
        Boolean twoWayMode = (Boolean) map.get("TwoWayMode");

        String stringLocation = (String) map.get("Location");
        String[] split = stringLocation.split(" ");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location location = new Location(world, x, y, z);

        TellerMachine tm = new TellerMachine(location);
        tm.setEnabled(enabled);
        tm.setTwoWayMode(twoWayMode);

        return tm;
    }

    // .getLocation() returns the location of the TellerMachine
    public Location getLocation() {
        return location;
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

    public void setTwoWayMode(Boolean bool) {
        twoWayMode = bool;
    }

    public Boolean getTwoWayMode() {
        return twoWayMode;
    }

}