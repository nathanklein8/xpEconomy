package com.gmail.neklein3.master;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        getLogger().info("registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("events have been registered");

        getLogger().info("adding commands...");
        this.getCommand("spawnEmployer").setExecutor(new EmployerCommand());
        getLogger().info("commands have been added");
    }

    @Override
    public void onDisable() {

    }
}