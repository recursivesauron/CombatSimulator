package com.rsauron.combatsimulator;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.Properties;

public class KafkaEventProducer {

    KafkaProducer<String, String> producer;

    public KafkaEventProducer(String host, String port){
        // create instance for properties to access producer configs
        Properties props = new Properties();
        props.put("bootstrap.servers", host + ":" + port); //Assign localhost id
        props.put("acks", "all"); //Set acknowledgements for producer requests.
        props.put("retries", 0); //If the request fails, the producer can automatically retry,
        props.put("batch.size", 16384); //Specify buffer size in config
        props.put("linger.ms", 1); //Reduce the no of requests less than 0
        props.put("buffer.memory", 33554432); //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }


    public void writeMove(JSONObject moveJSON){
        producer.send(new ProducerRecord<>("moves", moveJSON.getString("moveName"), moveJSON.toString()));
    }

    public void writeCharacter(Character character){
        JSONObject characterJSON = character.toJSON();
        producer.send(new ProducerRecord<>("characters", character.getName(), characterJSON.toString()));
    }

    public void writeEvent(JSONObject event){
        //expecting : {player, description}
        producer.send(new ProducerRecord<>("events", event.getString("player"), event.toString()));
    }

    public void closeConnection(){
        producer.close();
    }


}

