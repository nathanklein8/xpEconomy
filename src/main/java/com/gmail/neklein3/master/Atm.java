package com.gmail.neklein3.master;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
        }
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
                                main.getLogger().info("right clicked teller machine");
                            }
                        }
                    }
                }
            }
        } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {  // this toggles the atms on and off
            if (main.isTellerMachine(block)) {
                if (block != null) {
                    for (TellerMachine tm : main.TellerMachineList) {
                        if (tm.getLocation().equals(block.getLocation())) {
                            tm.setEnabled(!tm.getEnabled());
                        }
                    }
                }
            }
        }
    }

}
