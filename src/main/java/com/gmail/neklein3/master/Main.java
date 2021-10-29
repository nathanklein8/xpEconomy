package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    ArrayList<TellerMachine> TellerMachineList = new ArrayList<>();

    public void addTellerMachineToList(TellerMachine tellerMachine) {
        TellerMachineList.add(tellerMachine);
        config.set("TellerMachineList", TellerMachineList);
        saveConfig();
    }

    public void removeTellerMachine(TellerMachine tellerMachine) {
        TellerMachineList.remove(tellerMachine);
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

    // check if a block is a teller machine and change it's enabled to the specified value
    public void changeStatusIfTellerMachine(Block block, Boolean bool) {
        if (isSign(block)) {
            for (TellerMachine tm : TellerMachineList) {
                if (tm.getLocation().equals(block.getLocation())) {
                    tm.setEnabled(bool);
                }
            }
        }
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

    @Override
    public void onEnable() {

        getLogger().info("Registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands...");
        this.getCommand("atmTwoWayMode").setExecutor(new ConfigCommands(this));
        getLogger().info("Commands registered.");

        getLogger().info("Registering config defaults...");
        config.addDefault("TellerMachineList", TellerMachineList);
        config.addDefault("atmTwoWayMode", true);
        saveConfig();
        getLogger().info("Default Config registered.");

    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        saveConfig();
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
