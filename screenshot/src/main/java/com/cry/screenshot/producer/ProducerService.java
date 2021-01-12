package com.cry.screenshot.producer;

import com.alibaba.fastjson.JSON;
import com.cry.screenshot.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ProducerService {
    private static ProducerService producerService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @PostConstruct
    public void init(){
        producerService=this;
    }
    public void sendMessage(String roomid,String username,String usermessage){
        UserMessage userMessage = new UserMessage();
        userMessage.setUsername(username);
        userMessage.setMessage(usermessage);
        kafkaTemplate.send(roomid, JSON.toJSONString(userMessage));
    }

    public static ProducerService getProducerService() {
        return producerService;
    }
}
