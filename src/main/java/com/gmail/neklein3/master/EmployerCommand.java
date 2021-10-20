package com.gmail.neklein3.master;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.Collection;

public class EmployerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (((Player) sender).getPlayer());
            if (p != null) {
                p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                Collection<Entity> nearbyEntities = p.getWorld().getNearbyEntities(p.getLocation(), 1, 1, 1);
                nearbyEntities.removeIf(entity -> entity.getType() != EntityType.VILLAGER);
                for (Entity e : nearbyEntities) {
                    Villager v = (Villager) e;
                    v.setAI(false);
                    v.setCustomName("Job Contractor");
                    v.setCustomNameVisible(true);
                    v.setCollidable(false);
                    v.setInvulnerable(true);
                }
            }
            return true;
        }
        return false;
    }
}
