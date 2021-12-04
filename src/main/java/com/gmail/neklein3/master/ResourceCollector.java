package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
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

    public void applyUI(Player player) {

        Inventory resourceCollectorGUI = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Resource Collector");

        Inventory resourceCollectorGUIPWA = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Resource Collector");

        ItemStack collectResourcesItem = new ItemStack(Material.CHEST, 1);
        ItemMeta collectResourcesItemMeta = collectResourcesItem.getItemMeta();
        collectResourcesItemMeta.setDisplayName(ChatColor.BLUE + "Collect resources");
        collectResourcesItem.setItemMeta(collectResourcesItemMeta);

        ItemStack newJobItem = new ItemStack(Material.GREEN_CONCRETE, 1);
        ItemMeta newJobItemMeta = newJobItem.getItemMeta();
        newJobItemMeta.setDisplayName(ChatColor.GREEN + "Create resource collection job");
        newJobItem.setItemMeta(newJobItemMeta);

        int i = 0;
        if (main.ResourceCollectionJobList != null) {
            for (ResourceCollectionJob job : main.ResourceCollectionJobList) {
                if (i < 18) {
                    resourceCollectorGUI.setItem(i, job.getJobIcon());
                    resourceCollectorGUIPWA.setItem(i, job.getJobIcon());
                    i++;
                }
            }
        }

        resourceCollectorGUIPWA.setItem(18, newJobItem);
        resourceCollectorGUIPWA.setItem(26, collectResourcesItem);

        if (main.isPWA(player)) {
            player.openInventory(resourceCollectorGUIPWA);
        } else {
            player.openInventory(resourceCollectorGUI);
        }
    }
}