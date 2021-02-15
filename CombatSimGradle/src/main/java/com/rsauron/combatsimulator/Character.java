package com.rsauron.combatsimulator;


import com.rsauron.combatsimulator.mapper.CharacterMapper;
import com.rsauron.combatsimulator.mapper.MoveMapper;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Character {
    private final int id;
    private final String name;
    private final String characterClass;
    private int hp;
    private final int hpLimit;
    private List<Move> moveset;
    private List<Character> enemies;
    private boolean isDead;
    protected SqlSession sqlSession;
    protected KafkaEventProducer kafkaEventProducer;

    public Character(int id, String name, String characterClass, int hpLimit){
        this.id = id;
        this.name = name;
        this.characterClass = characterClass;
        this.hp = hpLimit;
        this.hpLimit = hpLimit;
        this.isDead = false;
    }

    public void bindSQLSession(SqlSession sqlSession){this.sqlSession = sqlSession;}
    public void bindKafkaEventProducer(KafkaEventProducer kafkaEventProducer){this.kafkaEventProducer = kafkaEventProducer;}
    public int getID(){return id;}
    public String getName(){return name;}
    public String getCharacterClass(){return characterClass;}
    public int getHP(){return hp;}
    public int getHPLimit(){return hpLimit;}
    public List<Move> getMoves(){return moveset;}
    public List<Character> getEnemies(){return enemies;}
    public boolean isDead(){return isDead;}
    public void updateEnemies(List<Character> enemies){this.enemies = enemies;}

    public void initializeMoveset(){
        MoveMapper MOVE_MAPPER = sqlSession.getMapper(MoveMapper.class);
        this.moveset = MOVE_MAPPER.getMovesByCharacterName(name);
    }

    public void initializeEnemies(HashMap<Integer, Character> characterIDs){
        CharacterMapper CHARACTER_MAPPER = sqlSession.getMapper(CharacterMapper.class);
        List<Integer> enemyIDs = CHARACTER_MAPPER.getEnemiesByID(this.id);

        ArrayList<Character> enemyList = new ArrayList<>();
        for(Integer enemyID : enemyIDs)
            enemyList.add(characterIDs.get(enemyID));

        this.enemies = enemyList;
    }

    public void increaseHP(int increaseValue){
        hp = Math.min(hpLimit, hp + increaseValue);
    }

    public void decreaseHP(int decreaseValue){
        if(hp - decreaseValue <= 0) {
            hp = 0;
            die();
        }
        else
            hp -= decreaseValue;
    }

    //String object structure will be "move" -> Move, and "target" -> Character
    public abstract HashMap<String, Object> determineMove();

    public abstract void takeTurn();

    public abstract JSONObject toJSON();

    public void useMove(Move move, Character target){
        //executes the move and sends the executed move to kafka
        switch (move.getType()) {
            case DAMAGE, RAGE -> target.decreaseHP(move.getValue());
            case HEALING -> target.increaseHP(move.getValue());
            default -> System.out.println("Unknown move type???" + move.getType());
        }
    }

    public void die(){
        this.isDead = true;
        System.out.println(this.name + " has died.");

        JSONObject deathEvent = new JSONObject();
        deathEvent.put("player", this.getName());
        deathEvent.put("description", "has died.");
        deathEvent.put("timestamp", System.currentTimeMillis());
        this.kafkaEventProducer.writeEvent(deathEvent);
    }

    public String toString(){
        return "{name: '" + name + "', hp: " + hp + "/" + hpLimit + ", dead: " + isDead + "}";
    }
}

