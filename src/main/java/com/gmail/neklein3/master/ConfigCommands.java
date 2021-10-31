package com.gmail.neklein3.master;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // atm toggle mode command
        if (command.getName().equalsIgnoreCase("atmTwoWayMode")) {
            // if they sent arguments
            if (args.length > 0) {
                if (args[0].equals("on")) {
                    Main.config.set("atmTwoWayMode", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled ATM Two Way Mode");
                    }
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equals("off")) {
                    Main.config.set("atmTwoWayMode", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled ATM Two Way Mode");
                    }
                    Main.saveConfigFile();
                    return true;
                }
            }
        }
        return false;
    }
}
