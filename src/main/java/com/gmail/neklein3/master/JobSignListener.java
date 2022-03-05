package com.gmail.neklein3.master;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class JobSignListener implements Listener {

    Main main;
    JobSignListener(Main m) {
        main = m;
    }

    public void placeNewJobSign(Event e, Block block) {
        if (e instanceof SignChangeEvent) {
            SignChangeEvent event = (SignChangeEvent) e;
            JobSign js = new JobSign(block.getLocation());
            js.setEnabled(true);
            main.addJobSignToList(js);

            event.setLine(0, "* Exp Economy *");
            event.setLine(1, "Resource");
            event.setLine(2, "Collector");
            event.setLine(3, js.getStatusString());
        }
    }

    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        // only oped players or pwa can create job signs
        if (e.getPlayer().isOp() || main.isPWA(e.getPlayer())) {
            if(e.getLine(0) != null && e.getLine(0).equals("[resource]")) {
                placeNewJobSign(e, block);
            }
        }
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (main.isJobSign(block)) {
            if (main.isPWA(e.getPlayer()) || e.getPlayer().isOp()) {
                main.removeIfJobSign(block);
            }
        }
    }
}