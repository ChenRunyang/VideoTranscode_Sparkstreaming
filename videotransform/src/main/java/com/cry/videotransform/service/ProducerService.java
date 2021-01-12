package com.cry.videotransform.service;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class ProducerService {
    private static ProducerService producerService;

@Autowired
    private KafkaTemplate kafkaTemplate;

@PostConstruct
    public void init(){
    producerService=this;
}

    public void sendData(String filekey, byte[] filevalue){
    kafkaTemplate.send("transQue",filekey,filevalue);
}

    public static ProducerService getProducerService() {
        return producerService;
    }

    public void sendH264Data(String filekey,byte[] filevalue) {kafkaTemplate.send("H264Que",filekey,filevalue);}

    public void sendAVSData(String filekey,byte[] filevalue) {kafkaTemplate.send("AVSQue",filekey,filevalue);}

    public void sendVP8Data(String filekey,byte[] filevalue) {kafkaTemplate.send("VP8Que",filekey,filevalue);}
}

