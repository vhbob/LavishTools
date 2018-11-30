package com.vhbob.lavishextras;

import com.vhbob.lavishextras.Listeners.Chopper;
import com.vhbob.lavishextras.Listeners.Harvester;
import com.vhbob.lavishextras.Listeners.NoDamage;
import com.vhbob.lavishextras.Listeners.StoreBlock;
import com.vhbob.lavishextras.commands.Condense;
import com.vhbob.lavishextras.commands.GiveChopper;
import com.vhbob.lavishextras.commands.GiveHarvester;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new NoDamage(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Chopper(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Harvester(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new StoreBlock(this), this);
        Bukkit.getPluginCommand("GiveHarvester").setExecutor(new GiveHarvester(this));
        Bukkit.getPluginCommand("GiveChopper").setExecutor(new GiveChopper(this));
        Bukkit.getPluginCommand("CondenseChest").setExecutor(new Condense(this));
        saveResource("config.yml", false);
        Bukkit.getConsoleSender().sendMessage("Lavish Extras has been " + ChatColor.GREEN + " enabled!" );
    }

}
