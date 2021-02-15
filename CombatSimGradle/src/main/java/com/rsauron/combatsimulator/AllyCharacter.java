package com.rsauron.combatsimulator;


import com.rsauron.combatsimulator.mapper.CharacterMapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AllyCharacter extends Character{
    private int ally;
    private int mana;
    private final int manaLimit;
    private List<Character> allies;

    public AllyCharacter(
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
        this.mana = manaLimit;
        this.manaLimit = manaLimit;
    }

    public void initializeAllies(HashMap<Integer, Character> characterIDs){
        CharacterMapper CHARACTER_MAPPER = sqlSession.getMapper(CharacterMapper.class);
        List<Integer> allyIDs = CHARACTER_MAPPER.getAlliesByID(this.getID());

        ArrayList<Character> allyList = new ArrayList<>();
        for(Integer allyID : allyIDs)
            allyList.add(characterIDs.get(allyID));

        this.allies = allyList;
    }

    public JSONObject toJSON(){
        JSONObject result = new JSONObject();
        result.put("characterName", this.getName());
        result.put("characterClass", this.getCharacterClass());
        result.put("hp", this.getHP());
        result.put("hpLimit", this.getHPLimit());
        result.put("mana", mana);
        result.put("manaLimit", manaLimit);
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }

    public void updateAllies(List<Character> allies){this.allies = allies;}

    public void takeTurn(){
        HashMap<String, Object> desiredAction = determineMove();
        Move desiredMove = (Move)desiredAction.get("move");
        Character desiredTarget = (Character)desiredAction.get("target");
        adjustMana(desiredMove);
        System.out.println(getName() + " uses " + desiredMove.getName() + " on " + desiredTarget);
        useMove((Move)desiredAction.get("move"), desiredTarget);
        this.kafkaEventProducer.writeCharacter(this);
        this.kafkaEventProducer.writeCharacter(desiredTarget);

        JSONObject enhancedMove = desiredMove.toJSON();
        enhancedMove.put("caster", this.getName());
        enhancedMove.put("target", desiredTarget.getName());
        this.kafkaEventProducer.writeMove(enhancedMove);
    }

    //since we already check if we have enough mana before making the move
    //here we can just adjust mana if the move has a cost value for it.
    public void adjustMana(Move move){
        if(move.getManaCost() > 0){
            this.mana -= move.getManaCost();
        }
    }

    public HashMap<String, Object> determineMove(){
        List<Character> injuredAllies = allies.stream()
                .filter(ally -> ally.getHP() < ally.getHPLimit() && !ally.isDead())
                .collect(Collectors.toList());

        boolean preferHealing = injuredAllies.size() > 0;
        boolean shouldHeal = false;
        boolean canHeal = false;

        if(preferHealing)
            shouldHeal = randomHealMoveChance();

        if(shouldHeal) {
            canHeal = this.getMoves().stream()
                    .filter(move -> move.getType() == Move.Type.HEALING)
                    .anyMatch(move -> mana >= move.getManaCost()); //equivalent to count = 0
        }

        if(canHeal){
            //choose a random healing type move that's within mana constraint
            List<Move> usableMoves = this.getMoves().stream()
                    .filter(move -> move.getType() == Move.Type.HEALING)
                    .filter(move -> mana >= move.getManaCost())
                    .collect(Collectors.toList());

            //should be a random move
            Move move = usableMoves.get(ThreadLocalRandom.current().nextInt(0, usableMoves.size()));

            //should be random ally
            Character target = injuredAllies.get(ThreadLocalRandom.current().nextInt(0, injuredAllies.size()));

            HashMap<String, Object> result = new HashMap<>();
            result.put("move", move);
            result.put("target", target);
            return result;
        }
        else{
            //choose a random damage type move
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

    public boolean randomHealMoveChance(){
        return ThreadLocalRandom.current().nextInt(0, 101) <= 80;
    }

    public String toString(){
        return "{name: '"
                + this.getName()
                + "', hp: "
                + this.getHP()
                + "/"
                + this.getHPLimit()
                + ", mana: " + this.mana
                + "/"
                + this.manaLimit
                + ", dead: "
                + this.isDead()
                + "}";
    }
}

