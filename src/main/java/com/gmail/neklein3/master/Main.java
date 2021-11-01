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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    List<ExchangeTerminal> ExchangeTerminalList = new ArrayList<>();

    public void addExchangeTerminalToList(ExchangeTerminal exchangeTerminal) {
        ExchangeTerminalList.add(exchangeTerminal);
        config.set("ExchangeTerminalList", ExchangeTerminalList);
        saveConfigFile();
    }

    public void removeIfExchangeTerminal(Block block) {
        if (isSign(block)) {
            ExchangeTerminalList.removeIf(et -> et.getLocation().equals(block.getLocation()));
            config.set("ExchangeTerminalList", ExchangeTerminalList);
            saveConfigFile();
        }
    }

    public ExchangeTerminal getExchangeTerminal(Location l) {
        for (ExchangeTerminal et : ExchangeTerminalList) {
            if (et.getLocation().equals(l)) {
                return et;
            }
        }
        return null;
    }

    public Boolean isExchangeTerminal(Block block) {
        if (isSign(block)) {
            for (ExchangeTerminal et : ExchangeTerminalList) {
                if (et.getLocation().equals(block.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addTellerMachineToList(TellerMachine tellerMachine) {
        TellerMachineList.add(tellerMachine);
        config.set("TellerMachineList", TellerMachineList);
        saveConfigFile();
    }

    public void removeIfTellerMachine(Block block) {
        if (isSign(block)) {
            TellerMachineList.removeIf(tm -> tm.getLocation().equals(block.getLocation()));
            config.set("TellerMachineList", TellerMachineList);
            saveConfigFile();
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

    // when calling this method, you HAVE to check if (isBanker(player) != null) first!!!!!!!!!!!!!!!
    public Boolean isBanker(Player player) {
        if (getBankerUUIDString() != null) {
            return player.getUniqueId().toString().equals(getBankerUUIDString());
        }
        return null;
    }

    // before calling, check if null
    public String getBankerUUIDString() {
        if (config.get("Banker") != null) {
            return (String) config.get("Banker");
        }
        return null;
    }

    // before calling, check if null
    public Boolean getExchangeTerminalTwoWayMode() {
        if (config.get("exchangeTerminalTwoWayMode") != null) {
            return (Boolean) config.get("exchangeTerminalTwoWayMode");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {

        getLogger().info("Serializing config...");
        ConfigurationSerialization.registerClass(TellerMachine.class, "TellerMachine");
        ConfigurationSerialization.registerClass(ExchangeTerminal.class, "ExchangeTerminal");
        getLogger().info("Loading config...");
        loadConfig();
        saveConfigFile();
        if (config.get("TellerMachineList") != null) {
            TellerMachineList = (List<TellerMachine>) config.get("TellerMachineList");
        }
        if (config.get("ExchangeTerminalList") != null) {
            ExchangeTerminalList = (List<ExchangeTerminal>) config.get("ExchangeTerminalList");
        }
        getLogger().info("Config loaded.");

        getLogger().info("Registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        Bukkit.getPluginManager().registerEvents(new AtmMenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BankMenuListener(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands...");
        this.getCommand("exchangeTerminalTwoWayMode").setExecutor(new ConfigCommands(this));
        this.getCommand("assignBanker").setExecutor(new ConfigCommands(this));
        getLogger().info("Commands registered.");

    }

    @Override
    public void onDisable() {
        getLogger().info("Backing up config...");
        config.set("TellerMachineList", TellerMachineList);
        saveConfigFile();
        getLogger().info("Config has been backed up.");
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}