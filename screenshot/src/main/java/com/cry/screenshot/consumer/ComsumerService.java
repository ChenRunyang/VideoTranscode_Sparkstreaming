package com.cry.screenshot.consumer;

import com.cry.screenshot.model.ConsumerModel;
import com.cry.screenshot.service.WebSocket;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.cry.screenshot.service.WebSocket.*;

@Component
@EnableScheduling
public class ComsumerService {
    private static ComsumerService comsumerService;
    private static ArrayList<ConsumerModel> consumerModels;
    private String userlist="";

    @PostConstruct
    public void init(){
        comsumerService=this;
        consumerModels= new ArrayList<>();
    }

    public void addUser(WebSocket user)
    {
        System.out.println("A user log");
        ConsumerModel tmp=new ConsumerModel();
        Properties prop=new Properties();
        List<String> topics =new ArrayList<>();
        prop.put("bootstrap.servers","127.0.0.1:9092");
        prop.put("auto.offset.reset","latest");
        prop.put("group.id",user.getCurrectuser());
        prop.put("enable.auto.commit","true");
        prop.put("auto.commit.interval","1S");
        prop.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        tmp.setProperties(prop);
        tmp.setConsumer(new KafkaConsumer<String, String>(prop));
        topics.add(user.getCurrectroom());
        tmp.getConsumer().subscribe(topics);
        tmp.setWebSocket(user);
        consumerModels.add(tmp);
        prop.clear();
        topics.clear();
    }

    public void deleteUser(WebSocket user)
    {
        for (ConsumerModel consumer:consumerModels
             ) {
            if(consumer.getWebSocket().equals(user))
            {
                consumer.getConsumer().unsubscribe();
                consumerModels.remove(consumer);
            }
        }
    }

    @Scheduled(cron = "*/4 * * * * ?")
    public void executeconsumerservice() throws IOException {
        System.out.println("reading data");
        userlist="";
        CopyOnWriteArraySet<WebSocket> webSockets=getWebSockets();
            for(ConsumerModel consumer:consumerModels)
            {
                userlist=userlist.concat(consumer.getWebSocket().getCurrectuser()+",");
                System.out.println(userlist);
            }
            for(ConsumerModel consumer:consumerModels)
            {
                ConsumerRecords<String,String> records= consumer.getConsumer().poll(100);  //每隔一秒拉取一次
                records.forEach(record->{
                    try {
                        consumer.getWebSocket().pushMessage("0",record.value());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                consumer.getWebSocket().pushMessage("1", "{\"usercount\":"+String.valueOf(getOnlineCount())+"}");
                consumer.getWebSocket().pushMessage("2","{\"userlist\":"+"\""+userlist+"\"}");
            }
    }
    public static ComsumerService getComsumerService() {
        return comsumerService;
    }
}
