package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    ArrayList<Location> atmLocations = new ArrayList<>();

    public void addATMLocation(Location location) {
        // adds location to atm locations if it's not already in the list
        if (!(atmLocations.contains(location))) {
            atmLocations.add(location);
            config.set("atmLocations", atmLocations);
            saveConfig();
        }
    }

    public void removeATMLocation(Location location) {
        // removes location from the list if it is an atm location
        if (atmLocations.contains(location)) {
            atmLocations.remove(location);
            config.set("atmLocations", atmLocations);
            saveConfig();
        }
    }

    public boolean isATMLocation(Location location) {
        return atmLocations.contains(location);
    }

    @Override
    public void onEnable() {

        getLogger().info("registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        getLogger().info("events have been registered");

        getLogger().info("adding commands...");
        this.getCommand("atmTwoWayMode").setExecutor(new ConfigCommands(this));
        getLogger().info("commands have been added");

        getLogger().info("adding config defaults...");
        config.addDefault("atmLocations", atmLocations);
        config.addDefault("atmTwoWayMode", true);
        saveConfig();
        getLogger().info("config has been defaulted");

    }

    @Override
    public void onDisable() {

    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
