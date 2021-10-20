package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends JavaPlugin implements Listener {

    private File atmFile;
    private YamlConfiguration modifyAtmFile;

    @Override
    public void onEnable() {

        //
        System.out.println("registering files");
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            initiateFiles();
        } catch (IOException e){
            e.printStackTrace();
        }


        getLogger().info("registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        getLogger().info("events have been registered");

        getLogger().info("adding commands...");
        this.getCommand("spawnEmployer").setExecutor(new EmployerCommand());
        getLogger().info("commands have been added");
    }

    @Override
    public void onDisable() {

    }

    public YamlConfiguration modifyAtmFile() { return modifyAtmFile; }
    public File getAtmFile() { return atmFile; }

    public void initiateFiles() throws IOException {
        atmFile = new File(Bukkit.getServer().getPluginManager().getPlugin("xpEconomy").getDataFolder(), "atmFile.yml");
        if (!atmFile.exists()) {
            atmFile.createNewFile();
            modifyAtmFile().save(this.getAtmFile());
        }
        modifyAtmFile = YamlConfiguration.loadConfiguration(atmFile);
        modifyAtmFile().save(this.getAtmFile());
        //end of initiateFiles
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
