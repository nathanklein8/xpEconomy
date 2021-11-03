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

        // assign banker command
        if (command.getName().equalsIgnoreCase("assignBanker")) {

            // if they sent arguments
            if (args.length > 0) {

                // this part removes the banker
                if (args[0].equalsIgnoreCase("clear")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getUniqueId().toString().equals(main.getBankerUUIDString())) {
                            p.sendMessage(ChatColor.RED + "The Banker role has been cleared,");
                            p.sendMessage(ChatColor.RED + "You are no longer the Banker");
                        }
                    }
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

        // display the current toggle settings
        if (command.getName().equalsIgnoreCase("xpEconomySettings")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (main.getUniversalATMCreation() != null) {
                    if (main.getUniversalATMCreation()) {
                        p.sendMessage("Universal ATM Creation: " + ChatColor.GREEN + "ON");
                    } else {
                        p.sendMessage("Universal ATM Creation: " + ChatColor.RED + "OFF");
                    }
                } else {
                    p.sendMessage("Universal ATM Creation: " + ChatColor.LIGHT_PURPLE + "not set");
                }

                if (main.getUniversalATMDestruction() != null) {
                    if (main.getUniversalATMDestruction()) {
                        p.sendMessage("Universal ATM Destruction: " + ChatColor.GREEN + "ON");
                    } else {
                        p.sendMessage("Universal ATM Destruction: " + ChatColor.RED + "OFF");
                    }
                } else {
                    p.sendMessage("Universal ATM Destruction: " + ChatColor.LIGHT_PURPLE + "not set");
                }

                if (main.getUniversalExchangeTerminalCreation() != null) {
                    if (main.getUniversalExchangeTerminalCreation()) {
                        p.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.GREEN + "ON");
                    } else {
                        p.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.RED + "OFF");
                    }
                } else {
                    p.sendMessage("Universal Exchange Terminal Creation: " + ChatColor.LIGHT_PURPLE + "not set");
                }

                if (main.getUniversalExchangeTerminalDestruction() != null) {
                    if (main.getUniversalExchangeTerminalDestruction()) {
                        p.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.GREEN + "ON");
                    } else {
                        p.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.RED + "OFF");
                    }
                } else {
                    p.sendMessage("Universal Exchange Terminal Destruction: " + ChatColor.LIGHT_PURPLE + "not set");
                }

            } else {
                if (main.getUniversalATMCreation() != null) {
                    main.getLogger().info("Universal ATM Creation: " + main.getUniversalATMCreation().toString());
                } else {
                    main.getLogger().info("Universal ATM Creation: not set");
                }
                if (main.getUniversalATMDestruction() != null) {
                    main.getLogger().info("Universal ATM Destruction: " + main.getUniversalATMDestruction().toString());
                } else {
                    main.getLogger().info("Universal ATM Destruction: not set");
                }
                if (main.getUniversalExchangeTerminalCreation() != null) {
                    main.getLogger().info("Universal Exchange Terminal Creation: " + main.getUniversalExchangeTerminalCreation().toString());
                } else {
                    main.getLogger().info("Universal Exchange Terminal Creation: not set");
                }
                if (main.getUniversalExchangeTerminalDestruction() != null) {
                    main.getLogger().info("Universal Exchange Terminal Destruction: " + main.getUniversalExchangeTerminalDestruction().toString());
                } else {
                    main.getLogger().info("Universal Exchange Terminal Destruction: not set");
                }
            }
            return true;
        }

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

        // toggle universal exchange terminal creation
        if (command.getName().equalsIgnoreCase("universalExchangeTerminalCreation")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("universalExchangeTerminalCreation", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Universal Exchange Terminal Creation");
                    }
                    main.getLogger().info("Enabled Universal Exchange Terminal Creation");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("universalExchangeTerminalCreation", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Universal Exchange Terminal Creation");
                    }
                    main.getLogger().info("Disabled Universal Exchange Terminal Creation");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }

        // toggle universal exchange terminal mining
        if (command.getName().equalsIgnoreCase("universalExchangeTerminalDestruction")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("universalExchangeTerminalDestruction", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Universal Exchange Terminal Destruction");
                    }
                    main.getLogger().info("Enabled Universal Exchange Terminal Destruction");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("universalExchangeTerminalDestruction", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Universal Exchange Terminal Destruction");
                    }
                    main.getLogger().info("Disabled Universal Exchange Terminal Destruction");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }

        // toggle universal atm creation
        if (command.getName().equalsIgnoreCase("universalATMCreation")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("universalATMCreation", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Universal ATM Creation");
                    }
                    main.getLogger().info("Enabled Universal ATM Creation");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("universalATMCreation", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Universal ATM Creation");
                    }
                    main.getLogger().info("Disabled Universal ATM Creation");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }

        // toggle universal atm mining
        if (command.getName().equalsIgnoreCase("universalATMDestruction")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("on")) {
                    Main.config.set("universalATMDestruction", true);
                    if (sender instanceof Player) {
                        sender.sendMessage("Enabled Universal ATM Destruction");
                    }
                    main.getLogger().info("Enabled Universal ATM Destruction");
                    Main.saveConfigFile();
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    Main.config.set("universalATMDestruction", false);
                    if (sender instanceof Player) {
                        sender.sendMessage("Disabled Universal ATM Destruction");
                    }
                    main.getLogger().info("Disabled Universal ATM Destruction");
                    Main.saveConfigFile();
                    return true;
                }
            }
        }

        return false;
    }
}
