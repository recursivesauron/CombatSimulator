package com.rsauron.combatsimulator;


import org.json.JSONObject;

public class Move {
    public enum Type{
        DAMAGE,
        HEALING,
        RAGE
    }

    private final String name;
    private final Type type;
    private final int value;
    private final int manaCost;
    private final int rageCost;

    public Move(String name, Type type, int value, int manaCost, int rageCost){
        this.name = name;
        this.type = type;
        this.value = value;
        this.manaCost = manaCost;
        this.rageCost = rageCost;
    }

    public String getName(){return name;}
    public Type getType(){return type;}
    public int getValue(){return value;}
    public int getManaCost(){return manaCost;}
    public int getRageCost(){return rageCost;}

    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        result.put("moveName", name);
        result.put("type", type);
        result.put("value", value);
        result.put("manaCost", manaCost);
        result.put("rageCost", rageCost);
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }
}
