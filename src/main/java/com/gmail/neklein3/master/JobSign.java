package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("JobSign")
public class JobSign implements ConfigurationSerializable {

    static Main main;
    JobSign(Main m) {
        main = m;
    }

    Location location;

    public JobSign(Location l) {
        location = l;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Location", location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        return result;
    }

    public static JobSign deserialize(Map<String, Object> map) {
        String l = (String) map.get("Location");
        String[] split = l.split(" ");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location loc = new Location(world, x, y, z);
        return new JobSign(loc);
    }

    Location getLocation() {
        return location;
    }

}
