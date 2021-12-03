package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ResourceCollectorMenuListener implements Listener {

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

                    case GREEN_CONCRETE:


                    // exit ui
                    player.closeInventory();
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_OFF, 10, 1);
                }
            }
        }
    }
}