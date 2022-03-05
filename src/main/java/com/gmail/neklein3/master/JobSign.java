package com.gmail.neklein3.master;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("JobSign")
public class JobSign implements ConfigurationSerializable {

    Location location;
    Boolean enabled;

    public JobSign(Location l) {
        location = l;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Location", location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        result.put("Enabled", enabled);
        return result;
    }

    public static JobSign deserialize(Map<String, Object> map) {
        Boolean enabled = (Boolean) map.get("Enabled");
        String l = (String) map.get("Location");
        String[] split = l.split(" ");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location loc = new Location(world, x, y, z);
        JobSign jobSign = new JobSign(loc);
        jobSign.setEnabled(enabled);

        return jobSign;
    }

    Location getLocation() {
        return location;
    }

    // .setEnabled() sets the boolean status of enabled
    public void setEnabled(Boolean bool) {
        enabled = bool;
    }

    // .getEnabled() returns true/false status of enabled
    public Boolean getEnabled() {
        return enabled;
    }

    public String getStatusString() {
        if (enabled) {
            return "Status: " + ChatColor.GREEN + "Online!";
        } else {
            return "Status: " + ChatColor.RED + "Offline!";
        }
    }

    // returns the block at the location of the job sign
    public Block getBlock() {
        return location.getBlock();
    }

}
