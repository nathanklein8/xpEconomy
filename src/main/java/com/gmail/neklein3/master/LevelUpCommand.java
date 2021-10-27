package com.gmail.neklein3.master;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.Collection;

public class LevelUpCommand implements CommandExecutor {

    Main main;
    LevelUpCommand(Main m) {
        this.main = m;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            String name = args[0];
            for (Player p : main.getServer().getOnlinePlayers()) {
                if (p.getDisplayName().equals(name)) {
                    p.giveExpLevels(1);
                    return true;
                }
            }
        }
        return false;
    }
}
