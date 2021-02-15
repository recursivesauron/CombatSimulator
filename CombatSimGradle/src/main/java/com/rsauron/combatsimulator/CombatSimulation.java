package com.rsauron.combatsimulator;


import com.rsauron.combatsimulator.mapper.CharacterMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CombatSimulation {
    public static void main(String[] args) {
        //Create KafkaEventProducer
        KafkaEventProducer kafkaEventProducer = new KafkaEventProducer("kafka", "29092");
        List<Character> everyone = new ArrayList<>();
        List<Character> hostiles;
        List<Character> friendlies;
        try  {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new
                    SqlSessionFactoryBuilder().build(inputStream);

            try(SqlSession session = sqlSessionFactory.openSession()) {
                CharacterMapper CHARACTER_MAPPER = session.getMapper(CharacterMapper.class);
                everyone = CHARACTER_MAPPER.getAllCharacters();
                HashMap<Integer, Character> characterIDs = new HashMap<>();

                for(Character individual : everyone){
                    characterIDs.put(individual.getID(), individual);
                }
                for(Character individual : everyone){
                    individual.bindKafkaEventProducer(kafkaEventProducer);
                    individual.bindSQLSession(session);
                    individual.initializeMoveset();
                    individual.initializeEnemies(characterIDs);

                    if(individual instanceof AllyCharacter){
                        AllyCharacter allyIndividual = (AllyCharacter) individual;
                        allyIndividual.initializeAllies(characterIDs);
                    }

                    //System.out.println(individual.toJSON());
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            hostiles = everyone.stream()
                                .filter(character -> character instanceof EnemyCharacter)
                                .collect(Collectors.toList());
            friendlies = everyone.stream()
                                 .filter(character -> character instanceof AllyCharacter)
                                 .collect(Collectors.toList());
        }
        
        //run simulation
        try {
            boolean combatInProgress = true;
            long friendliesAlive;
            long hostilesAlive = hostiles.size();
            while (combatInProgress) {
                for (Character character : everyone) {
                    if (!character.isDead()) {
                        character.takeTurn();
                    }

                    friendliesAlive = friendlies.stream()
                            .filter(ally -> !ally.isDead())
                            .count();

                    hostilesAlive = hostiles.stream()
                            .filter(enemy -> !enemy.isDead())
                            .count();

                    if (friendliesAlive == 0 || hostilesAlive == 0) {
                        combatInProgress = false;
                        break;
                    }


                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException iex) {
                        combatInProgress = false;
                        break;
                    }
                }
            }

            if (hostilesAlive == 0) {
                System.out.println("Combat over. Friendlies win.");
            } else {
                System.out.println("Combat over. Hostiles win.");
            }
        }
        catch(Exception ex){
            System.err.println("An unknown exception occurred during the combat simulation: " + ex.getMessage());
        }
        finally {
            kafkaEventProducer.closeConnection();
        }
    }
}
