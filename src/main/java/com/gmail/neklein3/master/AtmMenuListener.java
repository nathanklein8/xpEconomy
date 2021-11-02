package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AtmMenuListener implements Listener {

    private Main main;

    public AtmMenuListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GRAY + "Bank ATM")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        player.sendMessage("withdrawal");
                        break;
                    case PURPLE_CONCRETE:
                        player.sendMessage("deposit");
                        break;
                    default:
                        return;

                }
            }
        } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GRAY + "Bank ATM Sub-Menu")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        player.sendMessage("withdrawal of cash");
                        //withdraw cash
                        player.closeInventory();
                        break;
                    case PURPLE_CONCRETE:
                        player.sendMessage("deposit of cash");
                        //deposit cash
                        player.closeInventory();
                        break;
                    default:
                        return;
                }
            }
        }
    }


}
