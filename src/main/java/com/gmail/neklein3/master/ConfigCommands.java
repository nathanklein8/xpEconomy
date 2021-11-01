package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ConfigCommands implements CommandExecutor {

    Main main;
    ConfigCommands(Main m) {
        this.main = m;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // exchange Terminal toggle mode command
        if (command.getName().equalsIgnoreCase("exchangeTerminalTwoWayMode")) {
            // if they sent arguments
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("exchangeTerminalTwoWayMode", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Exchange Terminal Two Way Mode");
                    }
                    main.getLogger().info("Enabled Exchange Terminal Two Way Mode");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("exchangeTerminalTwoWayMode", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Exchange Terminal Two Way Mode");
                    }
                    main.getLogger().info("Disabled Exchange Terminal Two Way Mode");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }

        // assign banker command
        if (command.getName().equalsIgnoreCase("assignBanker")) {

            // if they sent arguments
            if (args.length > 0) {

                // this part removes the banker
                if (args[0].equalsIgnoreCase("removeBanker")) {
                    Main.config.set("Banker", null);
                    Main.saveConfigFile();
                    return true;
                }


                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (args[0].equals(p.getDisplayName())) {
                        // stuff to do when they do the right command
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (main.getBankerUUIDString() != null) {
                                if (main.getBankerUUIDString().equals(players.getUniqueId().toString())) { // find the player that is currently Banker, if any
                                    p.sendMessage(ChatColor.RED + "You are no longer the Banker");
                                    p.sendMessage(ChatColor.GRAY + args[0] + " is the new Banker");
                                }
                            }
                        }
                        p.sendMessage(ChatColor.GREEN + "You have been assigned the role of Banker!");
                        Main.config.set("Banker", p.getUniqueId().toString());
                        Main.saveConfigFile();
                        return true;
                    }
                }
            }
        }

        // toggle universal Bank Creation command
        if (command.getName().equalsIgnoreCase("universalBankCreation")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("universalBankCreation", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Universal Bank Creation");
                    }
                    main.getLogger().info("Enabled Universal Bank Creation");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("universalBankCreation", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Universal Bank Creation");
                    }
                    main.getLogger().info("Disabled Universal Bank Creation");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }



        return false;
    }
}
