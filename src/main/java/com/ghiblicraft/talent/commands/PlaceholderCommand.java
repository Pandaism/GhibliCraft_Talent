package com.ghiblicraft.talent.commands;

import com.ghiblicraft.talent.pages.TalentPage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceholderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            //move this statement to different location when ready
            TalentPage.getIns().openTalents(player);
        }
        return true;
    }
}
