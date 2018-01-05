package com.ghiblicraft.talent.types;

import java.util.ArrayList;

public class TalentTree {
    private ArrayList<Family> families;

    public TalentTree(ArrayList<Family> families) {
        this.families = families;
    }

    public ArrayList<Family> getFamilies() {
        return this.families;
    }
}
