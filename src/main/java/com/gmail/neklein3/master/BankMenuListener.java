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
                        main.transaction(main.cashToXp, player);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_ADMIRING_ITEM, 5, 1);
                        break;
                    case MAGENTA_CONCRETE:
                        player.sendMessage("convert to physical currency");
                        main.transaction(main.xpToCash, player);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, 5, 1);
                        break;
                    case RED_CONCRETE:
                        player.sendMessage("disable");

                        // gets the distance to the nearest TellerMachine
                        double lowestDistance = 10.0;
                        if (!(main.ExchangeTerminalList.isEmpty())) {
                            for (ExchangeTerminal et : main.ExchangeTerminalList) {
                                double distanceFromPlayer = et.getLocation().distance(player.getLocation());
                                if (distanceFromPlayer < lowestDistance) {
                                    lowestDistance = distanceFromPlayer;
                                }
                            }
                            // disables the atm that is closest
                            for (ExchangeTerminal et : main.ExchangeTerminalList) {
                                if (et.getLocation().distance(player.getLocation()) == lowestDistance) {
                                    ExchangeTerminal exchangeTerminal = main.getExchangeTerminal(et.getLocation());
                                    exchangeTerminal.setEnabled(false);
                                    main.changeSignText(exchangeTerminal.getBlock(), 3, exchangeTerminal.getStatusString());
                                    Main.saveConfigFile();
                                }
                            }
                        }

                        // exit ui
                        player.closeInventory();
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_OFF, 10, 1);
                    default:
                        return;
                }
            }
        }
    }

}
