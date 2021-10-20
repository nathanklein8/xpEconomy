package com.gmail.neklein3.master;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class Atm implements Listener {

    private Main main;
    public Atm(Main main) { this.main = main; }

    //atm creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Player player = e.getPlayer();
        if(e.getLine(0) != null && e.getLine(0).equals("[atm]")) {

        }

    }
}
