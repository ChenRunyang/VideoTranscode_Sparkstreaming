package com.cry.screenshot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cry.screenshot.consumer.ComsumerService;
import com.cry.screenshot.producer.ProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.cry.screenshot.consumer.ComsumerService.getComsumerService;
import static com.cry.screenshot.producer.ProducerService.getProducerService;

@ServerEndpoint("/websocket")
@Component
public class WebSocket {
    private static volatile  int onlineCount=0;

    private String currectroom;
    private String currectuser;
    //存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<WebSocket>();
    private Session session;

    @OnOpen
    public void OnOpen(Session session) throws Exception{
        System.out.println("Open");
        this.session=session;
        String str=session.getQueryString();
        String[] param=str.split("&");
        System.out.println(str);
        onlineCount++;
        this.setCurrectroom(param[0]);
        this.setCurrectuser(param[1]);
        this.session=session;
        webSockets.add(this);
        getComsumerService().addUser(this);
        System.out.println(this.getCurrectuser()+this.getCurrectroom());
    }

    @OnClose
    public void OnClose(){
        webSockets.remove(this);
        getComsumerService().deleteUser(this);
        onlineCount--;
        System.out.println("Close");
    }



    @OnMessage
    public void onMessage(String message,Session session){
        getProducerService().sendMessage(this.getCurrectroom(),this.getCurrectuser(),message);
    }

    @OnError
    public void OnError(Throwable error){
        error.printStackTrace();
    }

    ////////////////////////////////

    public void pushMessage(String flag,String message) throws IOException {
        JSONObject jsonObject = JSON.parseObject(message);
        if(flag.equals("0"))
        {
            this.session.getBasicRemote().sendText(flag+jsonObject.getString("username")+"："+jsonObject.getString("message"));
        }
        else if(flag.equals("1"))
        {
            this.session.getBasicRemote().sendText(flag+jsonObject.getString("usercount"));
        }
        else if(flag.equals("2"))
        {
            this.session.getBasicRemote().sendText(flag+jsonObject.getString("userlist"));
        }

    }

    public String getCurrectroom() {
        return currectroom;
    }

    public String getCurrectuser() {
        return currectuser;
    }

    public void setCurrectroom(String currectroom) {
        this.currectroom = currectroom;
    }

    public void setCurrectuser(String currectuser) {
        this.currectuser = currectuser;
    }

    public static CopyOnWriteArraySet<WebSocket> getWebSockets() {
        return webSockets;
    }

    public static int getOnlineCount() {
        return onlineCount;
    }
}
