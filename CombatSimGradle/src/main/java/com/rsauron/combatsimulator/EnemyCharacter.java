package com.rsauron.combatsimulator;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EnemyCharacter extends Character{
    private int ally;
    private int rage;
    private final int rageLimit;

    public EnemyCharacter(
            int id,
            String name,
            String characterClass,
            int hpLimit,
            int ally,
            int manaLimit,
            int rageLimit
    ){
        super(id, name, characterClass, hpLimit);
        this.ally = ally;
        this.rage = 10;
        this.rageLimit = rageLimit;
    }

    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        result.put("characterName", this.getName());
        result.put("characterClass", this.getCharacterClass());
        result.put("hp", this.getHP());
        result.put("hpLimit", this.getHPLimit());
        result.put("rage", rage);
        result.put("rageLimit", rageLimit);
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }


    public void takeTurn(){
        HashMap<String, Object> desiredAction = determineMove();
        Move desiredMove = (Move)desiredAction.get("move");
        Character desiredTarget = (Character)desiredAction.get("target");
        adjustRage(desiredMove);
        System.out.println(getName() + " uses " + desiredMove.getName() + " on " + desiredTarget);
        useMove((Move)desiredAction.get("move"), desiredTarget);

        this.kafkaEventProducer.writeCharacter(this);
        this.kafkaEventProducer.writeCharacter(desiredTarget);

        JSONObject enhancedMove = desiredMove.toJSON();
        enhancedMove.put("caster", this.getName());
        enhancedMove.put("target", desiredTarget.getName());
        this.kafkaEventProducer.writeMove(enhancedMove);
    }

    //since we already check if we have enough rage before making the move
    //here we can just adjust rage if the move has a cost value for it.
    public void adjustRage(Move move){
        if(move.getType() == Move.Type.RAGE){
            this.rage -= move.getRageCost();
        }
        else{
            this.rage = Math.min(rageLimit, rage + 10);
        }
    }

    public HashMap<String, Object> determineMove(){

        boolean shouldRage = randomRageMoveChance();
        boolean canRage = false;

        if(shouldRage) {
            canRage = this.getMoves().stream()
                    .filter(move -> move.getType() == Move.Type.RAGE)
                    .anyMatch(move -> rage >= move.getRageCost());
        }

        if(canRage){
            //choose a random rage type move that's within rage constraint
            List<Move> usableMoves = this.getMoves().stream()
                    .filter(move -> move.getType() == Move.Type.RAGE)
                    .filter(move -> rage >= move.getRageCost())
                    .collect(Collectors.toList());

            //should be a random move
            Move move = usableMoves.get(ThreadLocalRandom.current().nextInt(0, usableMoves.size()));

            //should be random enemy
            List<Character> validEnemies = this.getEnemies()
                    .stream()
                    .filter(enemy -> !enemy.isDead())
                    .collect(Collectors.toList());

            Character target = validEnemies.get(ThreadLocalRandom.current().nextInt(0, validEnemies.size()));

            HashMap<String, Object> result = new HashMap<>();
            result.put("move", move);
            result.put("target", target);
            return result;
        }
        else{
            //choose a random regular damage move
            List<Move> usableMoves = this.getMoves().stream()
                    .filter(move -> move.getType() == Move.Type.DAMAGE)
                    .collect(Collectors.toList());

            //should be a random move
            Move move = usableMoves.get(ThreadLocalRandom.current().nextInt(0, usableMoves.size()));

            //should be random enemy
            List<Character> validEnemies = this.getEnemies()
                    .stream()
                    .filter(enemy -> !enemy.isDead())
                    .collect(Collectors.toList());

            Character target = validEnemies.get(ThreadLocalRandom.current().nextInt(0, validEnemies.size()));

            HashMap<String, Object> result = new HashMap<>();
            result.put("move", move);
            result.put("target", target);
            return result;
        }
    }

    public boolean randomRageMoveChance(){
        return ThreadLocalRandom.current().nextInt(0, 101) <= 50;
    }

    public String toString(){
        return "{name: '"
                + this.getName()
                + "', hp: "
                + this.getHP()
                + "/"
                + this.getHPLimit()
                + ", rage: " + this.rage
                + "/"
                + this.rageLimit
                + ", dead: "
                + this.isDead()
                + "}";
    }
}

