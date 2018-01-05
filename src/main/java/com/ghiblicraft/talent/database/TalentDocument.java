package com.ghiblicraft.talent.database;

import org.bson.Document;

public class TalentDocument extends Document {

    public TalentDocument(String status, int level) {
        this.append("status", status)
                .append("level", level);
    }
}
