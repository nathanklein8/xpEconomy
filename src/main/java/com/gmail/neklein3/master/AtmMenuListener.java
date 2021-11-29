package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AtmMenuListener implements Listener {

    private Main main;

    public AtmMenuListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Bank ATM")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                ItemStack currentMoneyInBankItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
                ItemMeta currentMoneyInBankItemMeta = currentMoneyInBankItem.getItemMeta();
                currentMoneyInBankItemMeta.setDisplayName(ChatColor.DARK_BLUE + "Current Balance:");
                currentMoneyInBankItemMeta.setLore(Arrays.asList(ChatColor.BLUE + "" + main.getPlayerBalance(player)));
                currentMoneyInBankItem.setItemMeta(currentMoneyInBankItemMeta);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        main.transaction(TransactionType.WITHDRAW, player);
                        e.getClickedInventory().setItem(13, currentMoneyInBankItem);
                        break;
                    case PURPLE_CONCRETE:
                        main.transaction(TransactionType.DEPOSIT, player);
                        e.getClickedInventory().setItem(13, currentMoneyInBankItem);
                        break;
                    case RED_STAINED_GLASS_PANE:
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
                                    break;
                                }
                            }
                        }

                        // exit ui
                        player.closeInventory();
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_OFF, 10, 1);
                }
            }
        }
    }
}
