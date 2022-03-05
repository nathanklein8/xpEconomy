package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class ResourceCollector implements Listener {

    Main main;
    ResourceCollector(Main m) {
        this.main = m;
    }

    @EventHandler
    public void interactWithResourceCollector(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (entity instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader) entity;
            if (Objects.equals(trader.getName(), "Resource Collector")) {
                if (trader.isPersistent() && trader.isInvulnerable()) {
                    if (!(trader.isInvisible())) {
                        // right-clicked entity meets all the requirements
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                applyUI(p);
                            }
                        }.runTaskLater(this.main, 1);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (block != null) {
            if (main.isJobSign(block)) {
                JobSign jobSign = main.getJobSign(block.getLocation());
                if (jobSign.getEnabled()) {
                    if (e.getPlayer().isSneaking()) {
                        if (main.isPWA(e.getPlayer())) {
                            jobSign.setEnabled(false);
                            main.changeSignText(jobSign.getBlock(), 3, jobSign.getStatusString());
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 5, 1);
                            return;
                        }
                    }
                    if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        applyUI(e.getPlayer());
                    }
                } else {
                    if (main.isPWA(e.getPlayer())) {
                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 5, 1);
                        jobSign.setEnabled(true);
                        main.changeSignText(jobSign.getBlock(), 3, jobSign.getStatusString());
                    }
                }
            }
        }
    }

    public void applyUI(Player player) {

        Inventory resourceCollectorGUI = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Resource Collector");

        Inventory resourceCollectorGUIPWA = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Resource Collector");

        Inventory resourceCollectorGUIBanker = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Resource Collector");

        Inventory resourceCollectorGUIBoth = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Resource Collector");

        ItemStack collectResourcesItem = new ItemStack(Material.CHEST, 1);
        ItemMeta collectResourcesItemMeta = collectResourcesItem.getItemMeta();
        collectResourcesItemMeta.setDisplayName(ChatColor.BLUE + "Collect resources");
        collectResourcesItem.setItemMeta(collectResourcesItemMeta);

        ItemStack newJobItem = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta newJobItemMeta = newJobItem.getItemMeta();
        newJobItemMeta.setDisplayName(ChatColor.GREEN + "Create resource collection job");
        newJobItem.setItemMeta(newJobItemMeta);

        ItemStack delJobItem = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta delJobItemMeta = delJobItem.getItemMeta();
        delJobItemMeta.setDisplayName(ChatColor.RED + "Delete resource collection job");
        delJobItem.setItemMeta(delJobItemMeta);

        ItemStack setReward = new ItemStack(Material.SUNFLOWER, 1);
        ItemMeta setRewardMeta = setReward.getItemMeta();
        setRewardMeta.setDisplayName(ChatColor.YELLOW + "Assign Rewards");
        setRewardMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        setRewardMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        setReward.setItemMeta(setRewardMeta);

        int i = 0;
        if (main.ResourceCollectionJobList != null) {
            for (ResourceCollectionJob job : main.ResourceCollectionJobList) {
                if (i < 18) {
                    resourceCollectorGUI.setItem(i, job.getJobIcon());
                    resourceCollectorGUIPWA.setItem(i, job.getJobIcon());
                    resourceCollectorGUIBanker.setItem(i, job.getJobIcon());
                    resourceCollectorGUIBoth.setItem(i, job.getJobIcon());
                    i++;
                }
            }
        }

        // adds special items on the third row for special people
        //public works items
        resourceCollectorGUIPWA.setItem(18, newJobItem);
        resourceCollectorGUIPWA.setItem(19, delJobItem);
        resourceCollectorGUIPWA.setItem(26, collectResourcesItem);

        // banker items
        resourceCollectorGUIBanker.setItem(22, setReward);

        //both items
        resourceCollectorGUIBoth.setItem(18, newJobItem);
        resourceCollectorGUIBoth.setItem(19, delJobItem);
        resourceCollectorGUIBoth.setItem(26, collectResourcesItem);
        resourceCollectorGUIBoth.setItem(22, setReward);

        if (main.isPWA(player) && main.isBanker(player)) {
            // open both
            player.openInventory(resourceCollectorGUIBoth);
        } else if (main.isBanker(player)) {
            // open banker
            player.openInventory(resourceCollectorGUIBanker);
        } else if (main.isPWA(player)) {
            // open pwa
            player.openInventory(resourceCollectorGUIPWA);
        } else {
            player.openInventory(resourceCollectorGUI);
        }
    }
}