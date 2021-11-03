package com.gmail.neklein3.master;

import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
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

    public void setting(String settingPath, String arg) {
        if (arg.equalsIgnoreCase("on")) {
            Main.config.set(settingPath, true);
        } else if (arg.equalsIgnoreCase("off")) {
            Main.config.set(settingPath, false);
        }
        Main.saveConfigFile();
    }

    public void message(CommandSender sender, String arg, String message) {
        if (arg.equalsIgnoreCase("on")) {
            sender.sendMessage(message + " is now " + ChatColor.GREEN + arg.toUpperCase());
        }
        if (arg.equalsIgnoreCase("off")) {
            sender.sendMessage(message + ChatColor.RED + arg.toUpperCase());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        // assign banker command
        if (command.getName().equalsIgnoreCase("assignBanker")) {

            // if they sent arguments
            if (args.length > 0) {

                // this part removes the banker
                if (args[0].equalsIgnoreCase("clear")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getUniqueId().toString().equals(main.getBankerUUIDString())) {
                            p.sendMessage(ChatColor.RED + "You are no longer the Banker");
                        }
                    }
                    Main.config.set("Banker", null);
                    sender.sendMessage(ChatColor.RED + "The Banker role has been cleared,");
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

        // display the current toggle settings
        if (command.getName().equalsIgnoreCase("xpEconomySettings")) {
            //player sent no args
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "==================================");
                sender.sendMessage(ChatColor.GRAY + "Current " + ChatColor.DARK_PURPLE + "Xp" + ChatColor.LIGHT_PURPLE + "Economy" + ChatColor.GRAY + " Plugin Settings:");
                if (main.getUniversalATMCreation() != null) {
                    if (main.getUniversalATMCreation()) {
                        sender.sendMessage("Universal ATM Creation: " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage("Universal ATM Creation: " + ChatColor.RED + "OFF");
                    }
                } else {
                    sender.sendMessage("Universal ATM Creation: " + ChatColor.LIGHT_PURPLE + "not set");
                }
                if (main.getUniversalATMDestruction() != null) {
                    if (main.getUniversalATMDestruction()) {
                        sender.sendMessage("Universal ATM Destruction: " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage("Universal ATM Destruction: " + ChatColor.RED + "OFF");
                    }
                } else {
                    sender.sendMessage("Universal ATM Destruction: " + ChatColor.LIGHT_PURPLE + "not set");
                }
                if (main.getUniversalExchangeTerminalCreation() != null) {
                    if (main.getUniversalExchangeTerminalCreation()) {
                        sender.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.RED + "OFF");
                    }
                } else {
                    sender.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.LIGHT_PURPLE + "not set");
                }
                if (main.getUniversalExchangeTerminalDestruction() != null) {
                    if (main.getUniversalExchangeTerminalDestruction()) {
                        sender.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.RED + "OFF");
                    }
                } else {
                    sender.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.LIGHT_PURPLE + "not set");
                }
                sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "==================================");
                return true;

            } else if (args.length >= 3) {
                    if (args[0].equalsIgnoreCase("ExchangeTerminal")) {
                        if (args[1].equalsIgnoreCase("creation")) {
                            setting("universalExchangeTerminalCreation", args[2]);
                            message(sender, args[2], "Universal Exchange Terminal Creation");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("destruction")) {
                            setting("universalExchangeTerminalDestruction", args[2]);
                            message(sender, args[2], "Universal Exchange Terminal Destruction");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("twoWayMode")) {
                            setting("exchangeTerminalTwoWayMode", args[2]);
                            message(sender, args[2], "Exchange Terminal two way mode");
                        }
                    }
                    if (args[0].equalsIgnoreCase("ATM")) {
                        if (args[1].equalsIgnoreCase("creation")) {
                            setting("universalATMCreation", args[2]);
                            message(sender, args[2], "Universal ATM Creation");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("destruction")) {
                            setting("universalATMDestruction", args[2]);
                            message(sender, args[2], "Universal ATM Destruction");
                            return true;
                        }
                    }
            }
        }
        return false;
    }
}
