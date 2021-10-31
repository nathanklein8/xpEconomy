package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    static String s = Paths.get("").toAbsolutePath().toString();

    public static File file;
    public static FileConfiguration config;

    public void loadConfig() {
        if (file == null) {
            file = new File(s + "/plugins/xpEconomy", "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfigFile() {
        File file = new File(s + "/plugins/xpEconomy", "config.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<TellerMachine> TellerMachineList = new ArrayList<>();

    public void addTellerMachineToList(TellerMachine tellerMachine) {
        TellerMachineList.add(tellerMachine);
        config.set("TellerMachineList", TellerMachineList);
        saveConfig();
    }

    public void removeIfTellerMachine(Block block) {
        if (isSign(block)) {
            TellerMachineList.removeIf(tm -> tm.getLocation().equals(block.getLocation()));
            config.set("TellerMachineList", TellerMachineList);
            saveConfig();
        }
    }

    public TellerMachine getTellerMachine(Location l) {
        for (TellerMachine tm : TellerMachineList) {
            if (tm.getLocation().equals(l)) {
                return tm;
            }
        }
        return null;
    }

    // check if a block is a TellerMachine
    public Boolean isTellerMachine(Block block) {
        if (isSign(block)) {
            for (TellerMachine tm : TellerMachineList) {
                if (tm.getLocation().equals(block.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isSign(Block block) {
        return block.getBlockData() instanceof Sign || block.getBlockData() instanceof WallSign;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {

        getLogger().info("Serializing config...");
        ConfigurationSerialization.registerClass(TellerMachine.class, "TellerMachine");
        getLogger().info("Loading config...");
        loadConfig();
        saveConfigFile();
        if (config.get("TellerMachineList") != null) {
            TellerMachineList = (List<TellerMachine>) config.get("TellerMachineList");
        }
        getLogger().info("Config loaded.");

        getLogger().info("Registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        Bukkit.getPluginManager().registerEvents(new AtmMenuListener(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands...");
        this.getCommand("atmTwoWayMode").setExecutor(new ConfigCommands());
        getLogger().info("Commands registered.");


    }

    @Override
    public void onDisable() {
        config.set("TellerMachineList", TellerMachineList);
        saveConfigFile();
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
