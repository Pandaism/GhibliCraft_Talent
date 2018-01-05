package com.ghiblicraft.talent;

import com.ghiblicraft.talent.database.Mongo;
import com.ghiblicraft.talent.pages.TalentPage;
import com.ghiblicraft.talent.types.Family;
import com.ghiblicraft.talent.types.Node;
import com.ghiblicraft.talent.types.TalentTree;
import com.ghiblicraft.talent.types.Tier;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;

public class TalentManager {
    private static TalentManager ins = new TalentManager();
    public static TalentManager getIns() {return ins;}
    private ArrayList<Family> talentFamilies;

    public TalentManager() {
        this.talentFamilies = new ArrayList<>();

        retrieveTalentFromJSON();
    }

    public void addTalentPoint(Player player, int inc) {
        int current  = (Integer) Mongo.getIns().getDoc(player).get("points");
        Mongo.getIns().updateValue(player, "points", current + inc);
    }

    private void retrieveTalentFromJSON() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("./plugins/ghiblicraft/talents.json")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while((line = reader.readLine()) != null) {
                    if(!line.contains("//")) {
                        stringBuilder.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            JsonReader jsonReader = new JsonReader(new StringReader(stringBuilder.toString()));
            jsonReader.setLenient(true);

            TalentTree tree = new Gson().fromJson(jsonReader, TalentTree.class);
            this.talentFamilies.addAll(tree.getFamilies());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getTalentPoint(Player player) {
        Document doc = Mongo.getIns().getDoc(player);
        return String.valueOf(doc.get("points"));
    }

    public void talentOperations(Player player, ItemStack talentItem) {
        String talentName = talentItem.getItemMeta().getDisplayName().substring(2);
        if((Integer)Mongo.getIns().getDoc(player).get("points") > 0) {
            Node node = getNode(talentName);
            if(node != null) {
                if((Integer)Mongo.getIns().getTalentDoc(player, talentName).get("level") < node.getMaxLevel()) {
                    //increment talent
                    int currentLevel = (Integer)Mongo.getIns().getTalentDoc(player, talentName).get("level");
                    Mongo.getIns().updateValueInnerDoc(player, talentName, "level", currentLevel + 1);
                    //decrement points
                    int currentTalentPoint = (Integer)Mongo.getIns().getDoc(player).get("points");
                    Mongo.getIns().updateValue(player, "points", currentTalentPoint - 1);

                } else {
                    player.sendMessage(GhibliTalent.TAG_DENY + "This talent is max level.");
                }
            }
        } else {
            player.sendMessage(GhibliTalent.TAG_DENY + "Insufficient talent points.");
        }

        //Enable new talents if needed
        int totalLevel = 0;
        for(Family family : this.talentFamilies) {
            for(Tier tier : family.getTiers()) {
                for(String key : tier.getNodes().keySet()) {
                    for(Node node : tier.getNodes().get(key)) {
                        totalLevel += (Integer) Mongo.getIns().getTalentDoc(player, node.getName()).get("level");

                        if(Mongo.getIns().getTalentDoc(player, node.getName()).get("status").equals("disable")) {
                            if(requirementMet(player, node, totalLevel)) {
                                player.sendMessage(GhibliTalent.TAG_CONFIRM + "You have unlocked " + node.getName());
                            }
                        }
                    }
                }
            }
        }


        TalentPage.getIns().openTree(player.getOpenInventory().getTopInventory().getName(), player);
    }

    private boolean requirementMet(Player player, Node node, int totalLevel) {
        String[] requirements = node.getRequirements();
        ArrayList<Boolean> reqMet = new ArrayList<>();

        for(String requirement : requirements) {
            if(requirement.contains("Talent:")) {
                String reqTalentName = requirement.substring(requirement.indexOf(":") + 2);
                Node jsonNode = getNode(reqTalentName);

                if(jsonNode != null) {
                    if((Integer)Mongo.getIns().getTalentDoc(player, reqTalentName).get("level") == jsonNode.getMaxLevel()) {
                        reqMet.add(true);
                    } else {
                        reqMet.add(false);
                    }
                } else {
                    reqMet.add(false);
                }

            }

            if(requirement.contains("Level:")) {
                int requiredLevel = Integer.valueOf(requirement.substring(requirement.indexOf(":") + 2));

                if(totalLevel >= requiredLevel) {
                    reqMet.add(true);
                } else {
                    reqMet.add(false);
                }
            }
        }

        boolean broken = false;
        for(Boolean completed : reqMet) {
            if(!completed) {
                broken = true;
                break;
            }
        }

        if(!broken) {
            Mongo.getIns().updateValueInnerDoc(player, node.getName(), "status", "enable");
            return true;
        }


        return false;
    }

    private Node getNode(String talent) {
        for(Family family : this.talentFamilies) {
            for(Tier tier : family.getTiers()) {
                for(String key : tier.getNodes().keySet()) {
                    for(Node node : tier.getNodes().get(key)) {
                        if(node.getName().equals(talent)) {
                            return node;
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<String> getListofAvailableTalents() {
        ArrayList<String> listofTalents = new ArrayList<>();

        for(Family family : this.talentFamilies) {
            for(Tier tier : family.getTiers()) {
                for(String key : tier.getNodes().keySet()) {
                    for(Node node : tier.getNodes().get(key)) {
                        listofTalents.add(node.getName());
                    }
                }
            }
        }

        return listofTalents;
    }

    public ArrayList<Family> getTalentFamilies() {
        return this.talentFamilies;
    }
}
