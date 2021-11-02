package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
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
                        main.transaction(main.withdraw, player);
                        break;
                    case PURPLE_CONCRETE:
                        main.transaction(main.deposit, player);
                        break;
                    case RED_CONCRETE:
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
                            for (TellerMachine tellerMachine : main.TellerMachineList) {
                                if (tellerMachine.getLocation().distance(player.getLocation()) == lowestDistance) {
                                    TellerMachine atm = main.getTellerMachine(tellerMachine.getLocation());
                                    atm.setEnabled(false);
                                    main.changeSignText(atm.getBlock(), 3, atm.getStatusString());
                                    Main.saveConfigFile();
                                }
                            }
                        }
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
