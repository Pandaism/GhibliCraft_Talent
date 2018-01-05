package com.ghiblicraft.talent.commands;

import com.ghiblicraft.talent.database.Mongo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        //Do with ghibli
        Mongo.getIns().reviseData((Player) sender);


        return true;
    }
}
