package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            sender.sendMessage(message + " is now " + ChatColor.RED + arg.toUpperCase());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("summonResourceCollector")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                main.spawnResourceCollector(p);
                return true;
            }
        }

        // assign public works
        if (command.getName().equalsIgnoreCase("assignPublicWorkAdministrator")) {
            if (args.length > 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (args[0].equals(p.getDisplayName())) {
                        if (main.PublicWorkAdministratorList.contains(p.getUniqueId().toString())) {
                            p.sendMessage(ChatColor.RED + "This player is already a Public Work Administrator");
                            return true;
                        } else {
                            p.sendMessage(ChatColor.GREEN + "You have been assigned to be a Public Work Administrator!");
                            main.addPWAtoList(p);
                            return true;
                        }
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("removePublicWorkAdministrator")) {
            if (args.length > 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (args[0].equals(p.getDisplayName())) {
                        p.sendMessage(ChatColor.RED + "You are no longer a Public Work Administrator");
                        main.removePWAFromList(p);
                        return true;
                    }
                }
            }
        }
        // assign banker command
        if (command.getName().equalsIgnoreCase("assignBanker")) {
            // if they sent arguments
            if (args.length > 0) {

                // this part removes the banker
                if (args[0].equalsIgnoreCase("clear")) {
                    // let the old banker know they have been removed
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (main.isBanker(p)) {
                            p.sendMessage(ChatColor.RED + "You are no longer the Banker");
                        }
                    }
                    Main.config.set("Banker", null);
                    sender.sendMessage(ChatColor.RED + "The Banker role has been cleared,");
                    Main.saveConfigFile();
                    return true;
                }

                // if they input a name of online player
                for (Player p : Bukkit.getOnlinePlayers()) {

                    // new banker is already a banker
                    if (args[0].equals(p.getDisplayName()) && main.isBanker(p)) {
                        sender.sendMessage(ChatColor.RED + "This player is already the banker");
                        return true;
                    }

                    // input a new banker
                    if (args[0].equals(p.getDisplayName())) {
                        // let old banker know
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (main.isBanker(players)) {
                                players.sendMessage(ChatColor.RED + "You are no longer the Banker");
                                players.sendMessage(ChatColor.GRAY + args[0] + " is the new Banker");
                            }
                        }
                        // let new banker know
                        p.sendMessage(ChatColor.GREEN + "You have been assigned the role of Banker!");
                        Main.config.set("Banker", p.getUniqueId().toString());
                        Main.saveConfigFile();
                        return true;
                    }
                }

                // if they input a name of offline player
                for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                    if (args[0].equalsIgnoreCase(p.getName())) {
                        sender.sendMessage(args[0] + " is now the Banker.");
                        Main.config.set("Banker", p.getUniqueId().toString());
                        Main.saveConfigFile();
                        return true;
                    }
                }

            } else {
                // if they didn't send args
                sender.sendMessage(ChatColor.RED + "usage: /assignBanker <player/clear>");
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
                if (main.getExchangeTerminalTwoWayMode() != null) {
                    if (main.getExchangeTerminalTwoWayMode()) {
                        sender.sendMessage("Exchange Terminal two way mode: " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage("Exchange Terminal two way mode: " + ChatColor.RED + "OFF");
                    }
                } else {
                    sender.sendMessage("Exchange Terminal two way mode: " + ChatColor.LIGHT_PURPLE + "not set");
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
                            return true;
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
