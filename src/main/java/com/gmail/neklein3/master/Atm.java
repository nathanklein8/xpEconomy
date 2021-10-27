package com.gmail.neklein3.master;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class Atm implements Listener {

    Main main;
    Atm(Main main) { this.main = main; }

    //atm creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();

        if(e.getLine(0) != null && e.getLine(0).equals("[atm]")) {
            //logs the sign location in atmLocations in the config
            main.addATMLocation(block.getLocation());
            main.getLogger().info("placed atm");
        }
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();

        // remove the location of the sign from the atmLocation list if that location was already in the list
        if (block.getBlockData() instanceof Sign || block.getBlockData() instanceof WallSign) {
            main.getLogger().info("mined atm");
            main.removeATMLocation(block.getLocation());
        }
    }
}
