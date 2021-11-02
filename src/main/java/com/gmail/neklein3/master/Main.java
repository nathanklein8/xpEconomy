package com.gmail.neklein3.master;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    public void changeSignText(Block block, int line, String text) {
        if (isSign(block)) {
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
            sign.setLine(line, text);
            sign.update();
        }
    }

    public Boolean isSign(Block block) {
        return block.getBlockData() instanceof Sign || block.getBlockData() instanceof WallSign;
    }

    // before calling, check if null
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

    // before calling, check if null
    public Boolean getUniversalBankCreation() {
        if (config.get("universalBankCreation") != null) {
            return (Boolean) config.get("universalBankCreation");
        }
        return null;
    }

    public Boolean checkNameAndNumber(ItemStack item, String displayName, int customModelData) {
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().equals(displayName)) {
                        if (item.getItemMeta().hasCustomModelData()) {
                            return item.getItemMeta().getCustomModelData() == customModelData;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getCurrentBalance(Player player) {
        String uuid = player.getUniqueId().toString();
        String path = "bankAccounts." + uuid + ".currentBalance";
        return config.getInt(path);
    }

    public int getTotalBalance() {
        return config.getInt("totalInCirculation");
    }

    public void increaseTotalBalance() {
        config.set("totalInCirculation", getTotalBalance()+1);
        saveConfigFile();
    }

    public void decreaseTotalBalance() {
        config.set("totalInCirculation", getTotalBalance()-1);
        saveConfigFile();
    }

    public String withdraw = "withdraw";
    public String deposit = "deposit";
    public String xpToCash = "xpToCash";
    public String cashToXp = "cashToXp";
    public void transaction(String transactionType, Player p) {

        ItemStack currency = new ItemStack(Material.ACACIA_FENCE, 1);
        ItemMeta currencyMeta = currency.getItemMeta();
        String moneyName = "Money!";
        int moneyModelDataNumber = 69;
        currencyMeta.setDisplayName(moneyName);
        currencyMeta.setCustomModelData(moneyModelDataNumber);
        currency.setItemMeta(currencyMeta);

        String playerUUIDString = p.getUniqueId().toString();
        String path = "bankAccounts." + playerUUIDString + ".currentBalance";
        int playerBalance = config.getInt(path);

        if (transactionType.equals(xpToCash)) {
            if (p.getLevel() >= 1) {
                // give them money and lower their level by 1
                p.getInventory().addItem(currency);
                p.setLevel(p.getLevel() - 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, 5, 1);
                return;

            }
        }
        if (transactionType.equals(cashToXp)) {
            // check if they have cash in their inventory
            for (ItemStack item : p.getInventory().getContents()) {
                if (checkNameAndNumber(item, moneyName, moneyModelDataNumber)) {
                    // they have currency in their inventory
                    // remove 1 currency and add 1 level
                    item.setAmount(item.getAmount()-1);
                    p.setLevel(p.getLevel() + 1);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PIGLIN_ADMIRING_ITEM, 5, 1);
                    return;
                }
            }
        }
        if (transactionType.equals(withdraw)) {
            if (playerBalance > 0) {
                p.getInventory().addItem(currency);
                playerBalance--;
                config.set(path, playerBalance);
                decreaseTotalBalance();
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1);
                return;
            }
        }
        if (transactionType.equals(deposit)) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (checkNameAndNumber(item, moneyName, moneyModelDataNumber)) {
                    item.setAmount(item.getAmount()-1);
                    playerBalance++;
                    config.set(path, playerBalance);
                    increaseTotalBalance();
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 5, 1);
                }
            }
        }

        saveConfigFile();
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
        this.getCommand("universalBankCreation").setExecutor(new ConfigCommands(this));
        getLogger().info("Commands registered.");

    }

    @Override
    public void onDisable() {
        getLogger().info("Backing up config...");
        config.set("TellerMachineList", TellerMachineList);
        saveConfigFile();
        getLogger().info("Config has been backed up.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerUUIDString = p.getUniqueId().toString();

        config.addDefault(playerUUIDString + ".currentBalance", 0);
        config.addDefault("totalInCirculation", 0);
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}