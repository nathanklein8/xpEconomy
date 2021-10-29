package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Atm implements Listener {

    Main main;
    Atm(Main main) { this.main = main; }

    //atm creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        if(e.getLine(0) != null && e.getLine(0).equals("[atm]")) {
            //logs the sign location in atmLocations in the config
            TellerMachine atm = new TellerMachine(block);
            atm.setEnabled(true);
            main.addTellerMachineToList(atm);

            e.setLine(0, "* Exp Economy *");
            e.setLine(1, "BANK ATM");
            e.setLine(2, "");
            e.setLine(3, "Status:"  + atm.getStatus());

        }
    }

    //Atm UI
    public void applyAtmUI(Player player) {

        //Beginning
        Inventory gui = Bukkit.createInventory(null, 45, main.color("&2Bank ATM"));

        //Lores

        //ItemStacks
        ItemStack exchange;
        exchange = new ItemStack(Material.GREEN_WOOL);


        //Item Settings

        //Final

    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();

        // remove the location of the sign from the atmLocation list if that location was already in the list
        main.removeIfTellerMachine(block);
    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        // executes twice i think cause offhand
        // i have a code snippet to cancel that that ill add later

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) { // if the atm is on, it'll send a message in the console
            if (main.isTellerMachine(block)) {
                if (block != null) {
                    for (TellerMachine tm : main.TellerMachineList) {
                        if (tm.getLocation().equals(block.getLocation())) {
                            if (tm.getEnabled()) {
                                e.getPlayer().sendMessage("teller clicked!");
                                main.getLogger().info("right clicked teller machine");


                            }
                        }
                    }
                }
            }
        } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {  // this toggles the atms on and off for now
            if (main.isTellerMachine(block)) {
                if (block != null) {
                    for (TellerMachine tm : main.TellerMachineList) {
                        if (tm.getLocation().equals(block.getLocation())) {
                            if(tm.getEnabled()) {
                                e.getPlayer().sendMessage("teller disabled.");
                            } else {
                                e.getPlayer().sendMessage("teller enabled.");
                            }
                            tm.setEnabled(!tm.getEnabled());
                        }
                    }
                }
            }
        }
    }

}
