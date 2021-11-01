package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BankMenuListener implements Listener {

    Main main;
    BankMenuListener(Main m) {
        this.main = m;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.DARK_GRAY + "Bank Exchange Terminal")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case LIME_CONCRETE:
                        player.sendMessage("convert to xp");
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_ADMIRING_ITEM, 5, 1);
                        break;
                    case MAGENTA_CONCRETE:
                        player.sendMessage("convert to physical currency");
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, 5, 1);
                        break;
                    default:
                        return;
                }
            }
        }
    }

}
