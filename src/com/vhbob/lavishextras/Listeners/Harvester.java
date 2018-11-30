package com.vhbob.lavishextras.Listeners;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;
import org.bukkit.plugin.Plugin;
import java.util.List;
import java.util.UUID;

public class Harvester implements Listener {

    Plugin pl;

    public Harvester(Plugin plugin) {
        pl = plugin;
    }

    ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();

    public ItemStack getHarvester(Plugin plugin) {
        ItemStack harvester = new ItemStack(Material.SHEARS);
        ItemMeta harvesterm = harvester.getItemMeta();
        harvesterm.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Harvester.Name")));
        List<String> lore = plugin.getConfig().getStringList("Harvester.Lore");
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            lore.set(i, ChatColor.translateAlternateColorCodes('&', s));
        }
        harvesterm.setLore(lore);
        harvester.setItemMeta(harvesterm);
        return harvester;
    }

    @EventHandler (priority =  EventPriority.HIGHEST)
    public void onHarvest(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getInventory().getItemInMainHand() != null) {
            if (e.getPlayer().getInventory().getItemInMainHand().equals(getHarvester(pl)) && !e.isCancelled()) {
                if(e.getBlock().getState().getData() instanceof Crops) {
                    Crops crop = (Crops) e.getBlock().getState().getData();
                    if (!crop.getState().equals(CropState.RIPE)) {
                        return;
                    }
                    MaterialData state = e.getBlock().getState().getData();
                    Material type = e.getBlock().getType();
                    e.getBlock().breakNaturally();
                    e.getBlock().setType(type);
                    e.getBlock().getState().setData(state);
                    crop.setState(CropState.SEEDED);
                    e.setCancelled(true);
                } else if (e.getBlock().getState().getData() instanceof NetherWarts) {
                    NetherWarts warts = (NetherWarts) e.getBlock().getState().getData();
                    if (warts.getState().equals(NetherWartsState.RIPE)) {
                        MaterialData state = e.getBlock().getState().getData();
                        Material type = e.getBlock().getType();
                        e.getBlock().breakNaturally();
                        e.getBlock().setType(type);
                        warts.setState(NetherWartsState.SEEDED);
                        e.getBlock().getState().setData(state);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler (priority =  EventPriority.HIGHEST)
    public void harvest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block clicked = e.getPlayer().getTargetBlock(null, 10);
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                if (e.getPlayer().getInventory().getItemInMainHand().equals(getHarvester(pl)) && !e.isCancelled()) {
                    if(clicked.getState().getData() instanceof Crops) {
                        Crops crop = (Crops) clicked.getState().getData();
                        if (!crop.getState().equals(CropState.RIPE)) {
                            return;
                        }
                        MaterialData state = clicked.getState().getData();
                        Material type = clicked.getType();
                        clicked.breakNaturally();
                        clicked.setType(type);
                        clicked.getState().setData(state);
                        crop.setState(CropState.SEEDED);
                        e.setCancelled(true);
                    } else if (clicked.getState().getData() instanceof NetherWarts) {
                        NetherWarts warts = (NetherWarts) clicked.getState().getData();
                        if (warts.getState().equals(NetherWartsState.RIPE)) {
                            MaterialData state = clicked.getState().getData();
                            Material type = clicked.getType();
                            clicked.breakNaturally();
                            clicked.setType(type);
                            warts.setState(NetherWartsState.SEEDED);
                            clicked.getState().setData(state);
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
