package com.vhbob.lavishextras.Listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class StoreBlock implements Listener {

    Plugin pl;

    public StoreBlock(Plugin plugin) {
        pl = plugin;
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e){
        if (e.getBlockPlaced().getType().toString().contains("LOG")) {
            if (pl.getConfig().getStringList("Blocks.Placed") == null) {
                List<String> locs = new ArrayList<>();
                locs.add(e.getBlockPlaced().getLocation().toString());
            } else if (!pl.getConfig().getStringList("Blocks.Placed").contains(e.getBlockPlaced().getLocation().toString())){
                List<String> locs = pl.getConfig().getStringList("Blocks.Placed");
                locs.add(e.getBlockPlaced().getLocation().toString());
                pl.getConfig().set("Blocks.Placed", locs);
            }
            pl.saveConfig();
        }
    }

}
