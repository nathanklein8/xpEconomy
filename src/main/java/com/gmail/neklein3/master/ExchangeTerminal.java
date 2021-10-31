package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("ExchangeTerminal")
public class ExchangeTerminal implements ConfigurationSerializable {

    Location location;

    // used when creating a new instance of TellerMachine.  takes one input: the location of it
    public ExchangeTerminal(Location l) {
        location = l;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Location", location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        return result;
    }

    public static ExchangeTerminal deserialize(Map<String, Object> map) {
        String l = (String) map.get("Location");
        String[] split = l.split(" ");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        Location location = new Location(world, x, y, z);

        return new ExchangeTerminal(location);
    }

    public Location getLocation() {
        return location;
    }

}
