package com.ghiblicraft.talent.listeners;

import com.ghiblicraft.talent.GhibliTalent;
import com.ghiblicraft.talent.TalentManager;
import com.ghiblicraft.talent.database.Mongo;
import com.ghiblicraft.talent.pages.TalentPage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(Mongo.getIns().createDocument(e.getPlayer())) {
            e.getPlayer().sendMessage(GhibliTalent.TAG_INFO + "Initial talent data made.");
        }
    }

    @EventHandler
    public void onTalentTreeClick(InventoryClickEvent e) {
        if(e.getClickedInventory() != null) {
            if(e.getClickedInventory().getName().contains("Talent Tree")) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Talent Tree")) {

                    if (e.getCurrentItem().getData().getItemType() == Material.NAME_TAG) {
                        return;
                    }

                    if (e.getCurrentItem() != null && e.getCurrentItem().getData().getItemType() != Material.AIR) {
                        TalentPage.getIns().openTree(e.getCurrentItem().getItemMeta().getDisplayName(), (Player) e.getWhoClicked());
                    }
                } else {
                    if(e.getCurrentItem().hasItemMeta()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.GREEN + "")) {
                            TalentManager.getIns().talentOperations((Player)e.getWhoClicked(), e.getCurrentItem());
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED + "")) {
                            e.getWhoClicked().sendMessage(GhibliTalent.TAG_DENY + "You do not have the knowledge required for this talent.");
                        }

                    }
                }

            }
        }
    }
}
