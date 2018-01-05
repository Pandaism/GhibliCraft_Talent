package com.ghiblicraft.talent.types;

import java.util.ArrayList;
import java.util.Map;

public class Tier {
    private Map<String, ArrayList<Node>> nodes;

    public Tier(Map<String, ArrayList<Node>> nodes) {
        this.nodes = nodes;
    }

    public Map<String, ArrayList<Node>> getNodes() {
        return this.nodes;
    }
}
