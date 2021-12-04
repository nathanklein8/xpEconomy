package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ResourceCollectorMenuListener implements Listener {

    Material selectedMat = null;
    int amount = 0;

    Main main;
    public ResourceCollectorMenuListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Resource Collector")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case CHEST:
                        if (main.CompletedResourceCollectionJobList != null) {
                            for (ResourceCollectionJob job : main.CompletedResourceCollectionJobList) {
                                for (ItemStack stack : job.getJobBlocks()) {
                                    player.getWorld().dropItemNaturally(player.getLocation(), stack);
                                }
                            }
                        }
                        main.CompletedResourceCollectionJobList.clear();
                        Main.config.set("CompletedResourceCollectionJobList", main.CompletedResourceCollectionJobList);
                        Main.saveConfigFile();
                        player.closeInventory();
                        break;
                    case GREEN_CONCRETE:
                        player.closeInventory();
                        chooseBlockGUI(player);
                        break;
                }

                if (main.convertIconToJob(e.getCurrentItem()) != null) {
                    ResourceCollectionJob selectedJob = main.convertIconToJob(e.getCurrentItem());
                    main.completeJob(player, selectedJob);
                }

            }
        }

        if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Block")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                selectedMat = e.getCurrentItem().getType();
                player.closeInventory();
                chooseAmountGUI(player);
            }
        }

        if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Amount")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);



                switch (e.getCurrentItem().getType()) {
                    case RED_WOOL:
                        amount = 64;
                        break;
                    case ORANGE_WOOL:
                        amount = 128;
                        break;
                    case YELLOW_WOOL:
                        amount = 192;
                        break;
                    case LIME_WOOL:
                        amount = 256;
                        break;
                    case GREEN_WOOL:
                        amount = 320;
                        break;
                    case BLUE_WOOL:
                        amount = 384;
                        break;
                    case MAGENTA_WOOL:
                        amount = 448;
                        break;
                    case PURPLE_WOOL:
                        amount = 512;
                        break;
                    case BLACK_WOOL:
                        amount = 576;
                        break;

                }

                if (selectedMat != null && amount != 0) {
                    main.newResourceJob(selectedMat, amount);
                    player.closeInventory();
                }
            }
        }
    }

    ItemStack item(Material m, String name, String lore) {
        ItemStack result = new ItemStack(m, 1);
        ItemMeta meta = result.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GRAY + lore);
        meta.setLore(l);
        result.setItemMeta(meta);
        return result;
    }

    public void chooseBlockGUI(Player player) {
        Inventory selectBlock = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Select Block");

        ItemStack chooseBlock = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta chooseBlockMeta = chooseBlock.getItemMeta();
        chooseBlockMeta.setDisplayName(ChatColor.DARK_AQUA + "Choose block for New Collection Job");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Select an item from your inventory.");
        chooseBlockMeta.setLore(lore);
        chooseBlockMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        chooseBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        chooseBlock.setItemMeta(chooseBlockMeta);

        selectBlock.setItem(4, chooseBlock);

        player.openInventory(selectBlock);
    }

    public void chooseAmountGUI(Player player) {
        Inventory selectAmount = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Select Amount");

        selectAmount.setItem(0, item(Material.RED_WOOL, "1 stack", "64 blocks"));
        selectAmount.setItem(1, item(Material.ORANGE_WOOL, "2 stacks", "128 blocks"));
        selectAmount.setItem(2, item(Material.YELLOW_WOOL, "3 stacks", "192 blocks"));
        selectAmount.setItem(3, item(Material.LIME_WOOL, "4 stacks", "256 blocks"));
        selectAmount.setItem(4, item(Material.GREEN_WOOL, "5 stacks", "320 blocks"));
        selectAmount.setItem(5, item(Material.BLUE_WOOL, "6 stacks", "384 blocks"));
        selectAmount.setItem(6, item(Material.MAGENTA_WOOL, "7 stacks", "448 blocks"));
        selectAmount.setItem(7, item(Material.PURPLE_WOOL, "8 stacks", "512 blocks"));
        selectAmount.setItem(8, item(Material.BLACK_WOOL, "9 stacks", "576 blocks"));

        player.openInventory(selectAmount);
    }

}