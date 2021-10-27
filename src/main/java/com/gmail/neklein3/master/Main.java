package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

//    private File atmFile;
//    private YamlConfiguration modifyAtmFile;

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

//        System.out.println("registering files");
//        this.getConfig().options().copyDefaults();
//        saveDefaultConfig();
//        try {
//            initiateFiles();
//        } catch (IOException e){
//            e.printStackTrace();
//        }


        getLogger().info("registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        getLogger().info("events have been registered");

        getLogger().info("adding commands...");
        this.getCommand("spawnEmployer").setExecutor(new EmployerCommand());
        this.getCommand("giveMoneyXp").setExecutor(new LevelUpCommand(this));
        getLogger().info("commands have been added");

        config.addDefault("atmLocations", atmLocations);
        saveConfig();

    }

    @Override
    public void onDisable() {

    }

//    public YamlConfiguration modifyAtmFile() { return modifyAtmFile; }
//    public File getAtmFile() { return atmFile; }
//
//    public void initiateFiles() throws IOException {
//        atmFile = new File(Bukkit.getServer().getPluginManager().getPlugin("xpEconomy").getDataFolder(), "atmFile.yml");
//        if (!atmFile.exists()) {
//            atmFile.createNewFile();
//            modifyAtmFile().save(this.getAtmFile());
//        }
//        modifyAtmFile = YamlConfiguration.loadConfiguration(atmFile);
//        modifyAtmFile().save(this.getAtmFile());
//        //end of initiateFiles
//    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
