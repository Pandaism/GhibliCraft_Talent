package com.ghiblicraft.talent.pages;

import com.ghiblicraft.talent.TalentManager;
import com.ghiblicraft.talent.database.Mongo;
import com.ghiblicraft.talent.types.Family;
import com.ghiblicraft.talent.types.Node;
import com.ghiblicraft.talent.types.Tier;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class TalentPage {
    private static TalentPage ins = new TalentPage();

    public TalentPage() {

    }

    public void openTalents(Player player) {
        Inventory talentSheet = Bukkit.createInventory(null, 54, "Talent Tree");
        for(Family family : TalentManager.getIns().getTalentFamilies()) {
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL, 1);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName(ChatColor.AQUA + family.getName() + " Talent Tree");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.LIGHT_PURPLE + family.getDescription());
            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            talentSheet.setItem(family.getSlot(), book);
        }

        player.openInventory(talentSheet);

    }

    public void openTree(String spec, Player player) {
        Inventory talentSheet = Bukkit.createInventory(null, 54, spec);
        //setNavagationButtons

        //setTalentPointsShower
        ItemStack points = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta pointsMeta = points.getItemMeta();
        pointsMeta.setDisplayName(ChatColor.DARK_AQUA + "Talent Point");
        ArrayList<String> pointsLore = new ArrayList<>();
        pointsLore.add("Available Talent Points: " + TalentManager.getIns().getTalentPoint(player));
        pointsMeta.setLore(pointsLore);
        points.setItemMeta(pointsMeta);
        talentSheet.setItem(50, points);

        //setTalents
        for(Family family : TalentManager.getIns().getTalentFamilies()) {
            if(family.getName().equals(spec.substring(2, spec.indexOf(" ")))) {
                for (Tier tier : family.getTiers()) {
                    for (String key : tier.getNodes().keySet()) {
                        for (Node node : tier.getNodes().get(key)) {
                            Document doc = Mongo.getIns().getTalentDoc(player, node.getName());
                            ItemStack talent;
                            ItemMeta meta;
                            if (doc.get("status").equals("enable")) {
                                talent = new ItemStack(Material.ARROW, 1);
                                meta = talent.getItemMeta();
                                meta.setDisplayName(ChatColor.GREEN + node.getName());
                                ArrayList<String> description = new ArrayList<>();
                                if ((Integer) doc.get("level") == 0) {
                                    description.add("Current: ");
                                    description.add("Next: " + node.getDescription()[Integer.valueOf(doc.get("level").toString())]);
                                } else {
                                    description.add("Current: " + node.getDescription()[Integer.valueOf(doc.get("level").toString()) - 1]);
                                    if ((Integer) doc.get("level") < node.getMaxLevel()) {
                                        description.add("Next: " + node.getDescription()[Integer.valueOf(doc.get("level").toString())]);
                                    }

                                }
                                description.add("Level: " + doc.get("level").toString() + "/" + node.getMaxLevel());
                                meta.setLore(description);
                            } else {
                                talent = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 6);
                                meta = talent.getItemMeta();
                                meta.setDisplayName(ChatColor.RED + node.getName());
                                ArrayList<String> description = new ArrayList<>();
                                description.add(node.getDescription()[Integer.valueOf(doc.get("level").toString())]);
                                description.add("");
                                description.add(ChatColor.RED + "Req:" + Arrays.asList(node.getRequirements()));
                                meta.setLore(description);
                            }
                            talent.setItemMeta(meta);
                            talentSheet.setItem(node.getSlot(), talent);
                        }
                    }
                }
            }
        }

        player.openInventory(talentSheet);

    }

    public static TalentPage getIns() {
        return ins;
    }
}
