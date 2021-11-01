package com.gmail.neklein3.master;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AtmMenuListener implements Listener {

    Main main;
    AtmMenuListener(Main m) {
        this.main = m;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.DARK_GRAY + "Bank ATM")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        player.sendMessage("withdrawal");
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1);
                        break;
                    case PURPLE_CONCRETE:
                        player.sendMessage("deposit");
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 5, 1);
                        break;
                    case RED_CONCRETE:
                        player.sendMessage("disable");

                        // gets the distance to the nearest TellerMachine
                        double lowestDistance = 10.0;
                        if (!(main.TellerMachineList.isEmpty())) {
                            for (TellerMachine tm : main.TellerMachineList) {
                                double distanceFromPlayer = tm.getLocation().distance(player.getLocation());
                                if (distanceFromPlayer < lowestDistance) {
                                    lowestDistance = distanceFromPlayer;
                                }
                            }
                            // disables the atm that is closest
                            for (TellerMachine tellerMachine: main.TellerMachineList) {
                                if (tellerMachine.getLocation().distance(player.getLocation()) == lowestDistance) {
                                    TellerMachine atm = main.getTellerMachine(tellerMachine.getLocation());
                                    atm.setEnabled(false);
                                }
                            }
                        }
                        // exit ui
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 5, 1);
                        player.closeInventory();


                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_OFF, 10, 1);
                    default:
                        return;
                }
            }
        }
    }


}
