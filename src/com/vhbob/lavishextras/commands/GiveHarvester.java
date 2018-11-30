package com.vhbob.lavishextras.commands;

import com.vhbob.lavishextras.Listeners.Chopper;
import com.vhbob.lavishextras.Listeners.Harvester;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GiveHarvester implements CommandExecutor {

    Plugin pl;

    public GiveHarvester(Plugin plugin) {
        pl = plugin;
    }

    Harvester c = new Harvester(pl);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("giveharvester")) {
            if (commandSender.isOp() || commandSender.hasPermission("le.give.harvester")) {
                if (args.length == 1) {
                    if (Bukkit.getServer().getPlayer(args[0]) != null) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        boolean full = true;
                        for (ItemStack i : target.getInventory()) {
                            if (i == null || i.getType().equals(Material.AIR)) {
                                full = false;
                            }
                        }
                        if (full) {
                            target.getLocation().getWorld().dropItem(target.getLocation(), c.getHarvester(pl));
                        } else {
                            target.getInventory().addItem(c.getHarvester(pl));
                        }
                        commandSender.sendMessage(ChatColor.GREEN + "Successfully gave " + target.getName() + " a harvester");
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Could not find player: " + args[0]);
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /GiveHarvester (Player Name)");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Missing permission: le.give.harvester");
            }
        }
        return false;
    }

}
