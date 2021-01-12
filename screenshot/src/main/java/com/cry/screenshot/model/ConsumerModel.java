package com.cry.screenshot.model;

import com.cry.screenshot.service.WebSocket;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Properties;

public class ConsumerModel {
    private WebSocket webSocket;
    private Properties properties;
    private KafkaConsumer<String,String> consumer;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Properties getProperties() {
        return properties;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

}
