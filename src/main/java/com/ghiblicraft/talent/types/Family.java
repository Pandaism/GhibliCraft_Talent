package com.ghiblicraft.talent.types;

import java.util.ArrayList;

public class Family {
    private String name;
    private int slot;
    private String description;
    private ArrayList<Tier> tiers;

    public Family(String name, int slot, String description, ArrayList<Tier> tiers) {
        this.name = name;
        this.slot = slot;
        this.description = description;
        this.tiers = tiers;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSlot() {
        return this.slot;
    }

    public ArrayList<Tier> getTiers() {
        return this.tiers;
    }
}
