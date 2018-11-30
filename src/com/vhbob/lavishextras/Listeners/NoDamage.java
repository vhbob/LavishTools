package com.vhbob.lavishextras.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.Plugin;

public class NoDamage implements Listener {

    Plugin pl;

    public NoDamage(Plugin plugin) {
        pl = plugin;
    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent e) {
        if (e.getItem().equals(new Harvester(pl).getHarvester(pl)) || e.getItem().equals(new Chopper(pl).getChopper(pl))) {
            e.setCancelled(true);
        }
    }
}
