package com.ghiblicraft.talent;

import com.ghiblicraft.talent.commands.PlaceholderCommand;
import com.ghiblicraft.talent.commands.ReloadCommand;
import com.ghiblicraft.talent.database.Mongo;
import com.ghiblicraft.talent.listeners.GeneralListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class GhibliTalent extends JavaPlugin {
    public static String TAG_CONFIRM = ChatColor.BLUE + "[" + ChatColor.AQUA + "GhibliTalents" + ChatColor.BLUE + " ] " + ChatColor.GREEN;
    public static String TAG_DENY = ChatColor.BLUE + "[" + ChatColor.AQUA + "GhibliTalents" + ChatColor.BLUE + " ] " + ChatColor.RED;
    public static String TAG_INFO = ChatColor.BLUE + "[" + ChatColor.AQUA + "GhibliTalents" + ChatColor.BLUE + " ] " + ChatColor.YELLOW;

    @Override
    public void onEnable() {
        /*
        TODO
        **
        * Changable talents on the fly
         */
        Bukkit.getPluginCommand("reloadtalent").setExecutor(new ReloadCommand());
        Bukkit.getPluginCommand("pmenu").setExecutor(new PlaceholderCommand());
        Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
        Mongo.getIns().connect();
    }

    @Override
    public void onDisable() {
        Mongo.getIns().disconnect();
    }
}
