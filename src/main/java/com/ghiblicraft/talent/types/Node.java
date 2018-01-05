package com.ghiblicraft.talent.types;

public class Node {
    private String name;
    private int slot;
    private String[] requirements;
    private boolean parent;
    private String child;
    private int maxLevel;
    private String[] description;

    public Node(String name, int slot, String[] requirements, boolean parent, String child, int maxLevel, String[] description) {
        this.name = name;
        this.slot = slot;
        this.requirements = requirements;
        this.parent = parent;
        this.child = child;
        this.maxLevel = maxLevel;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public String[] getRequirements() {
        return requirements;
    }

    public boolean isParent() {
        return parent;
    }

    public String getChild() {
        return child;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String[] getDescription() {
        return description;
    }
}
