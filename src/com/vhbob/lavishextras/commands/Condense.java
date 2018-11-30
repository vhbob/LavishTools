package com.vhbob.lavishextras.commands;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class Condense implements CommandExecutor {

    Plugin pl;

    public Condense (Plugin plugin) {
        pl = plugin;
    }

    ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("CondenseChest")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if (p.isOp() || p.hasPermission("le.condense")) {
                    if (p.getTargetBlock(null, 10).getType().equals(Material.CHEST) || p.getTargetBlock(null, 10).getType().equals(Material.TRAPPED_CHEST)) {
                        UUID ownerId = aSkyBlockAPI.getOwner(p.getLocation());
                        Player owner = Bukkit.getPlayer(ownerId);
                        if (!aSkyBlockAPI.getTeamMembers(ownerId).contains(p.getUniqueId())) {
                            p.sendMessage(ChatColor.RED + "That is not your land!");
                            return false;
                        }
                        Container c = (Container) p.getTargetBlock(null, 10).getState();
                        Inventory ci = c.getInventory();
                        Inventory preInv = Bukkit.getServer().createInventory(null, 81);
                        Material[] comp = new Material[]{Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT, Material.INK_SACK, Material.DIAMOND
                                , Material.REDSTONE, Material.EMERALD};
                        ArrayList<Material> compressible = new ArrayList<>();
                        for (Material m : comp) {
                            compressible.add(m);
                        }
                        for (ItemStack i : ci.getContents()) {
                            if (i != null){
                                ItemStack pre = i.clone();
                                if (compressible.contains(i.getType())) {
                                    if (i.getAmount() > 8) {
                                        ItemStack item = new ItemStack(Material.AIR);
                                        int blocks = (i.getAmount() - (i.getAmount() % 9)) / 9;
                                        if (i.getType() == Material.INK_SACK) {
                                            Dye d = new Dye(i.getType());
                                            if (d.getColor().equals(DyeColor.BLUE)) {
                                                item.setType(Material.LAPIS_BLOCK);
                                                item.setAmount(blocks);
                                            }
                                        } else if (i.getType() == Material.DIAMOND) {
                                            item.setType(Material.DIAMOND_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.EMERALD) {
                                            item.setType(Material.EMERALD_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.REDSTONE) {
                                            item.setType(Material.REDSTONE_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.COAL) {
                                            item.setType(Material.COAL_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() != Material.INK_SACK) {
                                            String typeName = i.getType().toString().replace("INGOT", "BLOCK");
                                            item.setType(Material.getMaterial(typeName));
                                            item.setAmount(blocks);
                                        }
                                        pre.setAmount(pre.getAmount() % 9);
                                        preInv.addItem(item);
                                    }
                                }
                                preInv.addItem(pre);
                            }
                        }
                        Inventory inv2 = Bukkit.createInventory(null, 81);
                        for (ItemStack i : preInv.getContents()) {
                            if (i != null){
                                ItemStack pre = i.clone();
                                if (compressible.contains(i.getType())) {
                                    if (i.getAmount() > 8) {
                                        ItemStack item = new ItemStack(Material.AIR);
                                        int blocks = (i.getAmount() - (i.getAmount() % 9)) / 9;
                                        if (i.getType() == Material.INK_SACK) {
                                            Dye d = new Dye(i.getType());
                                            if (d.getColor().equals(DyeColor.BLUE)) {
                                                item.setType(Material.LAPIS_BLOCK);
                                                item.setAmount(blocks);
                                            }
                                        } else if (i.getType() == Material.DIAMOND) {
                                            item.setType(Material.DIAMOND_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.EMERALD) {
                                            item.setType(Material.EMERALD_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.REDSTONE) {
                                            item.setType(Material.REDSTONE_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() == Material.COAL) {
                                            item.setType(Material.COAL_BLOCK);
                                            item.setAmount(blocks);
                                        } else if (i.getType() != Material.INK_SACK) {
                                            String typeName = i.getType().toString().replace("INGOT", "BLOCK");
                                            item.setType(Material.getMaterial(typeName));
                                            item.setAmount(blocks);
                                        }
                                        pre.setAmount(pre.getAmount() % 9);
                                        inv2.addItem(item);
                                    }
                                }
                                inv2.addItem(pre);
                            }
                        }
                        int counter = 0;
                        for (ItemStack i : inv2.getContents()) {
                            if (i != null && !i.getType().equals(Material.AIR))
                                counter++;
                        }
                        if (counter <= ci.getSize()) {
                            ci.clear();
                            for (ItemStack i : inv2.getContents()) {
                                if (i != null)
                                    ci.addItem(i);
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.Condense_Success")));
                        } else {
                            p.sendMessage(ChatColor.RED + "Not enough space to condense!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You must be looking at a chest");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Missing permission: le.condense");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "You are not a player");
            }
        }
        return false;
    }

    public boolean hasSpace(Inventory i) {
        for (ItemStack it : i.getContents()) {
            if (it == null || it.getType().equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsItem(Inventory i, Material type) {
        for (ItemStack item : i.getContents()) {
            if (item != null && item.getType().equals(type))
                return true;
        }
        return false;
    }

    public int indexOfItem(Inventory i, Material type) {
        for (int count = 0; count < i.getSize(); count++) {
            if (i.getItem(count).getType().equals(type))
                return count;
        }
        return -1;
    }

}
