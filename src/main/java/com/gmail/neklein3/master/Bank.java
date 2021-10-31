package com.gmail.neklein3.master;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class Bank implements Listener {

    Main main;
    Bank(Main m) {
        this.main = m;
    }

    //bank sign creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        if(e.getLine(0) != null && e.getLine(0).equals("[atm]")) {
            //logs the sign location in atmLocations in the config
            TellerMachine atm = new TellerMachine(block.getLocation());
            atm.setEnabled(true);
            main.addTellerMachineToList(atm);

            e.setLine(0, "* Exp Economy *");
            e.setLine(1, "Exchange Terminal");
            e.setLine(2, "");
            e.setLine(3, "Status:"  + atm.getStatus());
        }
    }
}