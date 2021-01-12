package com.cry.screenshot.UserApi;

import com.alibaba.fastjson.JSONObject;
import com.cry.screenshot.producer.ProducerService;
import com.cry.screenshot.service.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class UserApi {

    @Autowired
    private WebSocket webSocket;

  //  @Autowired
   // DiscoveryClient discoveryClient;

}
