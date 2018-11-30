package com.vhbob.lavishextras.Listeners;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sapling;
import org.bukkit.material.Wood;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chopper implements Listener {

    Plugin pl;

    public Chopper(Plugin plugin) {
        pl = plugin;
    }

    ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();

    public ItemStack getChopper(Plugin plugin) {
        ItemStack chopper = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta chopperm = chopper.getItemMeta();
        chopperm.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Chopper.Name")));
        List<String> lore = plugin.getConfig().getStringList("Chopper.Lore");
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            lore.set(i, ChatColor.translateAlternateColorCodes('&', s));
        }
        chopperm.setLore(lore);
        chopper.setItemMeta(chopperm);
        return chopper;
    }

    @EventHandler (priority =  EventPriority.HIGHEST)
    public void onChop(BlockBreakEvent e) {
        Player p = e.getPlayer();
        boolean hasList = false;
        List<String> locs = new ArrayList<>();
        if (pl.getConfig().contains("Blocks.Placed")) {
            hasList = true;
            locs = pl.getConfig().getStringList("Blocks.Placed");
        }
        if (e.getBlock().getType().toString().contains("LOG") && (!hasList || !locs.contains(e.getBlock().getLocation().toString()))) {
            if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().equals(getChopper(pl)) && !e.isCancelled()) {
                Wood w = (Wood) e.getBlock().getState().getData();
                ArrayList<Block> lowest = new ArrayList<>();
                lowest.add(e.getBlock());
                Block b = e.getBlock();
                ArrayList<Block> blocksToBreak = new ArrayList<>();
                for (int i = 0; i < 26; i++) {
                    Block breaking = b;
                    while (breaking != null) {
                            breaking.breakNaturally();
                        boolean clear = false;
                        boolean add = false;
                        for (Block clow : lowest) {
                            if (breaking.getY() < clow.getY()) {
                                clear = true;
                            } else if (breaking.getY() == clow.getY()) {
                                add = true;
                            }
                        }
                        if (clear) {
                            lowest.clear();
                            lowest.add(breaking);
                        } else if (add)
                            lowest.add(breaking);
                        breaking = nearBlock(breaking);
                    }
                }
                for (Block lowBlock : lowest) {
                    if (!locs.contains(lowBlock.getLocation().toString())) {
                        e.getBlock().getWorld().getBlockAt(lowBlock.getLocation()).setType(Material.DIRT);
                        lowBlock.setType(Material.SAPLING);
                        BlockState bs = lowBlock.getState();
                        MaterialData md = bs.getData();
                        Sapling sp = (Sapling) md;
                        sp.setSpecies(w.getSpecies());
                        bs.setData((MaterialData) sp);
                        bs.update();
                        if (lowBlock.getLocation().getWorld().getBlockAt(lowBlock.getLocation().add(0, -1, 0)).getType() == Material.AIR) {
                            lowBlock.setType(Material.AIR);
                        }
                    }
                }
                e.setCancelled(true);
            }
        }
        if (e.getBlock().getType().toString().contains("LOG")) {
            locs.remove(e.getBlock().getLocation().toString());
            pl.getConfig().set("Blocks.Placed", locs);
            pl.saveConfig();
        }
    }

    public Block nearBlock(Block b) {
        for (int i = -1; i < 2; i++) {
            for (int i2 = -1; i2 < 2; i2++) {
                for (int i3 = -1; i3 < 2; i3++) {
                    Block test = b.getWorld().getBlockAt(b.getLocation().add(i, i2, i3));
                    if (test.getType().toString().contains("LOG") && !test.getType().toString().contains("STRIPPED")) {
                        return test;
                    }
                }
            }
        }
        return null;
    }

}
