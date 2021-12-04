package com.gmail.neklein3.master;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
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
    List<ResourceCollectionJob> CompletedResourceCollectionJobList = new ArrayList<>();
    List<ResourceCollectionJob> ResourceCollectionJobList = new ArrayList<>();
    List<JobSign> JobSignList = new ArrayList<>();
    List<String> PublicWorkAdministratorList = new ArrayList<>();
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

    String moneyName = "xpMoney";
    int moneyModelDataNumber = 69;
    Material moneyMaterial = Material.SUNFLOWER;

    public void transaction(TransactionType type, Player p) {

        ItemStack currency = new ItemStack(moneyMaterial, 1);
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
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 7, 1);
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
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 7, 10);
                    return;
                }
            }
        } else if (type == TransactionType.WITHDRAW) {
            if (playerBalance > 0) {
                p.getInventory().addItem(currency);
                decreaseBalance(p);
                decreaseTotalBalance();
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 7, 5);
                return;
            }
        } else if (type == TransactionType.DEPOSIT) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (checkMaterialNameNumber(item, moneyMaterial, moneyName, moneyModelDataNumber)) {
                    item.setAmount(item.getAmount()-1);
                    increaseBalance(p);
                    increaseTotalBalance();
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 7, 15);
                }
            }
        }
        saveConfigFile();
    }

    public void spawnResourceCollector(Player player) {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.WANDERING_TRADER);
        List<Entity> nearbyEntities = player.getNearbyEntities(1, 1, 1);
        nearbyEntities.removeIf(e -> !(e.getType().equals(EntityType.WANDERING_TRADER)));
        for (Entity e : nearbyEntities) {
            WanderingTrader trader = (WanderingTrader) e;
            trader.setCustomName("Resource Collector");
            trader.setInvulnerable(true);
            trader.setPersistent(true);
        }
    }

    public void addPWAtoList(Player player) {
        if (PublicWorkAdministratorList != null) {
            if (!(PublicWorkAdministratorList.contains(player.getUniqueId().toString()))) {
                PublicWorkAdministratorList.add(player.getUniqueId().toString());
                config.set("PublicWorkAdministratorList", PublicWorkAdministratorList);
                saveConfigFile();
            }
        }
    }

    public void removePWAFromList(Player player) {
        if (PublicWorkAdministratorList != null) {
            PublicWorkAdministratorList.removeIf(s -> s.equals(player.getUniqueId().toString()));
            config.set("PublicWorkAdministratorList", PublicWorkAdministratorList);
            saveConfigFile();
        }
    }

    public void addJobSignToList(JobSign jobSign) {
        if (JobSignList != null) {
            JobSignList.add(jobSign);
            config.set("JobSignList", JobSignList);
            saveConfigFile();
        }
    }

    public void removeJobSignFromList(JobSign jobSign) {
        if (JobSignList != null) {
            JobSignList.removeIf(js -> JobSignList.contains(jobSign));
            config.set("JobSignList", JobSignList);
            saveConfigFile();
        }
    }

    public boolean isPWA(Player player) {
        if (PublicWorkAdministratorList != null) {
            return PublicWorkAdministratorList.contains(player.getUniqueId().toString());
        }
        return false;
    }

    public void newResourceJob(Material m, int amount) {
        ResourceCollectionJob job = new ResourceCollectionJob(m, amount, false);
        if (ResourceCollectionJobList.size() < 18) {
            ResourceCollectionJobList.add(job);
            config.set("ResourceCollectionJobList", ResourceCollectionJobList);
            saveConfigFile();
        } else {
            this.getLogger().info("Resource Job List is full.  Cannot add new job.");
        }
    }

    public void removeResourceJob(ResourceCollectionJob job) {

        if (ResourceCollectionJobList != null) {
            for (ResourceCollectionJob rcj : ResourceCollectionJobList) {
                if (rcj.getMaterial() == job.getMaterial() && rcj.getAmount() == job.getAmount()) {
                    ResourceCollectionJobList.remove(ResourceCollectionJobList.indexOf(rcj));
                    break;
                }
            }

            config.set("ResourceCollectionJobList", ResourceCollectionJobList);
            saveConfigFile();
        }
    }

    public ResourceCollectionJob convertIconToJob(ItemStack icon) {
        ItemMeta meta = icon.getItemMeta();
        List<String> lore = meta.getLore();
        int reward;
        if (lore != null) {
            String[] amountLine = lore.get(0).split(" ");
            int amount = Integer.parseInt(amountLine[1]);
            String[] rewardLine = lore.get(1).split(" ");
            if (rewardLine[1].equals("not-set")) {
                reward = 0;
                getLogger().info("reward = 0");
            } else {
                reward = Integer.parseInt(rewardLine[1]);
                getLogger().info("reward = " + reward);
            }
            Material material = icon.getType();
            ResourceCollectionJob job = new ResourceCollectionJob(material, amount, false);
            job.setReward(reward);
            return job;
        }
        return null;
    }

    public boolean canPlayerCompleteJob(Player p, ResourceCollectionJob job) {
        int totalInInv = 0;
        for (ItemStack stack : p.getInventory()) {
            if (stack != null) {
                if (stack.getType().equals(job.getMaterial())) {
                    totalInInv = totalInInv + stack.getAmount();
                }
            }
        }
        return totalInInv >= job.getAmount();
    }

    public void completeJob(Player p, ResourceCollectionJob job) {
        if (canPlayerCompleteJob(p, job)) {
            if (job.getReward() != 0) {



                int numRemoved = 0;
                int totalToRemove = job.getAmount();

                while (numRemoved < totalToRemove) {
                    int index = p.getInventory().first(job.getMaterial());
                    numRemoved = numRemoved + p.getInventory().getItem(index).getAmount();
                    p.getInventory().setItem(index, null);
                }
                if (numRemoved == totalToRemove) {
                    job.setCompleted(true);
                    removeResourceJob(job);

                    ItemStack currency = new ItemStack(moneyMaterial, job.getReward());
                    ItemMeta currencyMeta = currency.getItemMeta();
                    currencyMeta.setDisplayName(moneyName);
                    currencyMeta.setCustomModelData(moneyModelDataNumber);
                    currencyMeta.addEnchant(Enchantment.DURABILITY, 3, true);
                    currencyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    currency.setItemMeta(currencyMeta);

                    p.getInventory().addItem(currency);

                    CompletedResourceCollectionJobList.add(job);
                    config.set("CompletedResourceCollectionJobList", CompletedResourceCollectionJobList);
                    saveConfigFile();
                } else if (numRemoved > totalToRemove){
                    int overflow = numRemoved - totalToRemove;
                    p.getInventory().addItem(new ItemStack(job.getMaterial(), overflow));
                    job.setCompleted(true);
                    removeResourceJob(job);
                    CompletedResourceCollectionJobList.add(job);
                    config.set("CompletedResourceCollectionJobList", CompletedResourceCollectionJobList);
                    saveConfigFile();
                }
            } else {
                p.sendMessage(ChatColor.RED + "This job has not been assigned a reward yet, please wait for the Banker to assign it's reward.");
                p.closeInventory();
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have enough resources to complete this Job!");
            p.closeInventory();
        }
        p.closeInventory();

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {

        getLogger().info("Serializing config...");
        ConfigurationSerialization.registerClass(TellerMachine.class, "TellerMachine");
        ConfigurationSerialization.registerClass(ExchangeTerminal.class, "ExchangeTerminal");
        ConfigurationSerialization.registerClass(JobSign.class, "JobSign");
        ConfigurationSerialization.registerClass(ResourceCollectionJob.class, "ResourceCollectionJob");
        getLogger().info("Loading config...");
        loadConfig();
        saveConfigFile();
        if (config.get("TellerMachineList") != null) {
            TellerMachineList = (List<TellerMachine>) config.get("TellerMachineList");
        }
        if (config.get("ExchangeTerminalList") != null) {
            ExchangeTerminalList = (List<ExchangeTerminal>) config.get("ExchangeTerminalList");
        }
        if (config.get("PublicWorkAdministratorList") != null) {
            PublicWorkAdministratorList = (List<String>) config.get("PublicWorkAdministratorList");
        }
        if (config.get("JobSignList") != null) {
            JobSignList = (List<JobSign>) config.get("JobSignList");
        }
        if (config.get("ResourceCollectionJobList") != null) {
            ResourceCollectionJobList = (List<ResourceCollectionJob>) config.get("ResourceCollectionJobList");
        }
        if (config.get("CompletedResourceCollectionJobList") != null) {
            CompletedResourceCollectionJobList = (List<ResourceCollectionJob>) config.get("CompletedResourceCollectionJobList");
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
        Bukkit.getPluginManager().registerEvents(new ResourceCollector(this), this);
        Bukkit.getPluginManager().registerEvents(new ResourceCollectorMenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JobSignListener(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands...");
        this.getCommand("assignBanker").setExecutor(new ConfigCommands(this));
        this.getCommand("assignPublicWorkAdministrator").setExecutor(new ConfigCommands(this));
        this.getCommand("removePublicWorkAdministrator").setExecutor(new ConfigCommands(this));
        this.getCommand("summonResourceCollector").setExecutor(new ConfigCommands(this));
        this.getCommand("xpEconomySettings").setExecutor(new ConfigCommands(this));
        getLogger().info("Commands registered.");

        saveConfigFile();
    }

    @Override
    public void onDisable() {
        getLogger().info("Backing up config...");
        config.set("TellerMachineList", TellerMachineList);
        config.set("ExchangeTerminalList", ExchangeTerminalList);
        config.set("PublicWorkAdministratorList", PublicWorkAdministratorList);
        config.set("JobSignList", JobSignList);
        config.set("ResourceCollectionJobList", ResourceCollectionJobList);
        config.set("CompletedResourceCollectionJobList", CompletedResourceCollectionJobList);
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