package com.ghiblicraft.talent.database;

import com.ghiblicraft.talent.GhibliTalent;
import com.ghiblicraft.talent.TalentManager;
import com.ghiblicraft.talent.types.Family;
import com.ghiblicraft.talent.types.Node;
import com.ghiblicraft.talent.types.Tier;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Mongo {
    private MongoClient mongo;
    private MongoDatabase database;
    private static Mongo ins = new Mongo();
    private MongoCollection<Document> collection;

    public static Mongo getIns() {
        return ins;
    }

    public void connect() {
        this.mongo = new MongoClient();
        this.database = this.mongo.getDatabase("ghiblicraft");
        create();
        this.collection = this.database.getCollection("GhibliTalents");
    }

    public void disconnect() {
        this.mongo.close();
    }

    private void create() {
        for(String name : this.database.listCollectionNames()) {
            if(name.equals("GhibliTalents")) {
                return;
            }
        }
        this.database.createCollection("GhibliTalents");
    }

    public boolean createDocument(Player player) {
        for(Document doc : this.collection.find()) {
            if(doc.get("uuid").equals(player.getUniqueId().toString())) {
                player.sendMessage(GhibliTalent.TAG_INFO + "Loading user talents");
                return false;
            }
        }

        Document doc = new Document("uuid", player.getUniqueId().toString())
                .append("spec", "none")
                .append("points", 0);

        for(Family family : TalentManager.getIns().getTalentFamilies()) {
            for(Tier tier : family.getTiers()) {
                for(String key : tier.getNodes().keySet()) {
                    for(Node node : tier.getNodes().get(key)) {
                        if(key.contains("Tier 1")) {
                            doc.append(node.getName(), new TalentDocument("enable", 0));
                        } else {
                            doc.append(node.getName(), new TalentDocument("disable", 0));
                        }
                    }
                }
            }
        }

        this.collection.insertOne(doc);

        return true;
    }

    public Document getDoc(Player player) {
        for(Document doc : this.collection.find()) {
            if(doc.get("uuid").equals(player.getUniqueId().toString())) {
                return doc;
            }
        }

        return null;
    }

    public Document getTalentDoc(Player player, String talent) {
        Document doc = getDoc(player);
        return (Document) doc.get(talent);
    }

    public boolean updateValue(Player player, String key, Object value) {
        UpdateResult result = this.collection.updateOne(new BasicDBObject().append("uuid", player.getUniqueId().toString()), new BasicDBObject().append("$set", new BasicDBObject().append(key, value)));

        return result.wasAcknowledged();
    }

    public boolean updateValueInnerDoc(Player player, String talent, String key, Object value) {
        UpdateResult result = this.collection.updateOne(new BasicDBObject().append("uuid", player.getUniqueId().toString()), new BasicDBObject().append("$set", new BasicDBObject().append(talent + "." + key, value)));


        return result.wasAcknowledged();
    }

    public void removeDocKey(Document doc, String key) {
        doc.remove(key);
    }

    private ArrayList<String> getDatabaseTalents(Document doc) {
        ArrayList<String> talents = new ArrayList<>();
        for(String key : doc.keySet()) {
            if(!key.equals("_id") && !key.equals("uuid") && !key.equals("spec") && !key.equals("points")) {
                talents.add(key);
            }
        }

        return talents;
    }

    public void reviseData(Player player) {
        /*
        Compare json to database
            if talent in data doesnt match json talent
                remove the talent or add
         */
        ArrayList<String> jsonTalent = TalentManager.getIns().getListofAvailableTalents();

        for(Document doc : this.collection.find()) {
            ArrayList<String> databaseTalents = getDatabaseTalents(doc);

            for(String dataTalent : databaseTalents) {
                for(String definedTalent : jsonTalent) {
                    if(dataTalent.equals(definedTalent)) {
                        jsonTalent.remove(dataTalent);
                        break;
                    }
                }
            }
//            //remove matches
//            for(String definedTalent : jsonTalent) {
//                for(String dataTalent : databaseTalents) {
//                    if(definedTalent.equals(dataTalent)) {
//                        databaseTalents.remove(dataTalent);
//                        jsonTalent.remove(definedTalent);
//                        break;
//                    }
//                }
//            }

//            //remove database Talents
//            for(String key : databaseTalents) {
//                this.removeDocKey(doc, key);
//                player.sendMessage(GhibliTalent.TAG_INFO + key + " is removed from database.");
//            }
//
//            for(String talent : jsonTalent) {
//                for(Family family : TalentManager.getIns().getTalentFamilies()) {
//                    for(Tier tier : family.getTiers()) {
//                        for(String key : tier.getNodes().keySet()) {
//                            for(Node node : tier.getNodes().get(key)) {
//                                if(node.getName().equals(talent)) {
//                                    if(key.contains("Tier 1")) {
//                                        doc.append(node.getName(), new TalentDocument("enable", 0));
//                                    } else {
//                                        doc.append(node.getName(), new TalentDocument("disable", 0));
//                                    }
//                                    player.sendMessage(GhibliTalent.TAG_INFO + node.getName() + " is added into database.");
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}
