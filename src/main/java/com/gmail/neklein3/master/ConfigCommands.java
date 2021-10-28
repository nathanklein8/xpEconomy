package com.gmail.neklein3.master;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigCommands implements CommandExecutor {

    Main main;
    ConfigCommands(Main m) {
        this.main = m;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // atm toggle mode command
        if (command.getName().equalsIgnoreCase("atmToggleMode")) {
            // if they sent arguments
            if (args.length > 0) {
                if (args[0].equals("on")) {
                    main.config.set("atmTwoWayMode", true);
                    main.saveConfig();
                    return true;
                } else if (args[0].equals("off")) {
                    main.config.set("atmTwoWayMode", false);
                    main.saveConfig();
                    return true;
                }
            }
        }
        return false;
    }
}
