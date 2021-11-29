package com.gmail.neklein3.master;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    public Boolean isBanker(Player player) {
        if (getBankerUUIDString() != null) {
            return player.getUniqueId().toString().equals(getBankerUUIDString());
        }
        return false;
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
    public Boolean getUniversalExchangeTerminalCreation() {
        if (config.get("universalExchangeTerminalCreation") != null) {
            return (Boolean) config.get("universalExchangeTerminalCreation");
        }
        return null;
    }

    public Boolean getUniversalExchangeTerminalDestruction() {
        if (config.get("universalExchangeTerminalDestruction") != null) {
            return (Boolean) config.get("universalExchangeTerminalDestruction");
        }
        return null;
    }

    public Boolean getUniversalATMCreation() {
        if (config.get("universalATMCreation") != null) {
            return (Boolean) config.get("universalATMCreation");
        }
        return null;
    }

    public Boolean getUniversalATMDestruction() {
        if (config.get("universalATMDestruction") != null) {
            return (Boolean) config.get("universalATMDestruction");
        }
        return null;
    }

    public Boolean checkMaterialNameNumber(ItemStack item, Material material, String displayName, int customModelData) {
        if (item != null) {
            if (item.getType().equals(material)) {
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
        }
        return false;
    }

    public int getTotalBalance() {
        return config.getInt("totalBalance");
    }

    public void increaseTotalBalance() {
        config.set("totalBalance", getTotalBalance()+1);
        saveConfigFile();
    }

    public void decreaseTotalBalance() {
        config.set("totalBalance", getTotalBalance()-1);
        saveConfigFile();
    }

    public int getPlayerBalance(Player p) {
        return config.getInt("bankAccounts." + p.getUniqueId().toString() + ".currentBalance");
    }

    public void increaseBalance(Player p) {
        String uuid = p.getUniqueId().toString();
        int currentBalance = getPlayerBalance(p);
        config.set("bankAccounts." + uuid + ".currentBalance", currentBalance + 1);
        saveConfigFile();
    }

    public void decreaseBalance(Player p) {
        String uuid = p.getUniqueId().toString();
        int currentBalance = getPlayerBalance(p);
        config.set("bankAccounts." + uuid + ".currentBalance", currentBalance - 1);
        saveConfigFile();
    }

    String moneyName = "Money";
    int moneyModelDataNumber = 69;
    Material moneyMaterial = Material.SUNFLOWER;

    public void transaction(TransactionType type, Player p) {

        ItemStack currency = new ItemStack(Material.SUNFLOWER, 1);
        ItemMeta currencyMeta = currency.getItemMeta();
        currencyMeta.setDisplayName(moneyName);
        currencyMeta.setCustomModelData(moneyModelDataNumber);
        currencyMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        currencyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        currency.setItemMeta(currencyMeta);

        int playerBalance = getPlayerBalance(p);

        if (type == TransactionType.XPTOCASH) {
            if (p.getLevel() >= 1) {
                // give them money and lower their level by 1
                p.getInventory().addItem(currency);
                p.setLevel(p.getLevel() - 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, 5, 1);
                return;

            }
        } else if (type == TransactionType.CASHTOXP) {
            // check if they have cash in their inventory
            for (ItemStack item : p.getInventory().getContents()) {
                if (checkMaterialNameNumber(item, moneyMaterial, moneyName, moneyModelDataNumber)) {
                    // they have currency in their inventory
                    // remove 1 currency and add 1 level
                    item.setAmount(item.getAmount()-1);
                    p.setLevel(p.getLevel() + 1);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PIGLIN_ADMIRING_ITEM, 5, 1);
                    return;
                }
            }
        } else if (type == TransactionType.WITHDRAW) {
            if (playerBalance > 0) {
                p.getInventory().addItem(currency);
                decreaseBalance(p);
                decreaseTotalBalance();
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1);
                return;
            }
        } else if (type == TransactionType.DEPOSIT) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (checkMaterialNameNumber(item, moneyMaterial, moneyName, moneyModelDataNumber)) {
                    item.setAmount(item.getAmount()-1);
                    increaseBalance(p);
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
        config.addDefault("totalBalance", 0);
        config.addDefault("exchangeTerminalTwoWayMode", true);
        config.addDefault("universalExchangeTerminalCreation", true);
        config.addDefault("universalExchangeTerminalDestruction", true);
        config.addDefault("universalATMCreation", true);
        config.addDefault("universalATMDestruction", true);
        saveConfigFile();
        getLogger().info("Config loaded.");

        getLogger().info("Registering events...");
        this.getServer().getPluginManager().registerEvents(new Bank(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Atm(this), this);
        Bukkit.getPluginManager().registerEvents(new AtmMenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BankMenuListener(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands...");
        this.getCommand("assignBanker").setExecutor(new ConfigCommands(this));
        this.getCommand("xpEconomySettings").setExecutor(new ConfigCommands(this));
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
    }

    @EventHandler
    public void onPlaceMoney(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (checkMaterialNameNumber(p.getInventory().getItemInMainHand(), moneyMaterial, moneyName, moneyModelDataNumber)) {
            if (e.getBlockPlaced().getType().equals(moneyMaterial)) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "please stop trying to place money");
            }
        }
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}