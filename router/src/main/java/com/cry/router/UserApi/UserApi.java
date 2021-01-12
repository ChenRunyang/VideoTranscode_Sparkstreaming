package com.cry.router.UserApi;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class UserApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/getlogin")
        public Object getcheck(@RequestParam(required = true,value = "name") String name, @RequestParam(required = true,value="password") String password){
            return restTemplate.getForObject("http://admin-server/api/loginserver?name="+name+"&password="+password, JSONObject.class);
    }

    @PostMapping("/getregist")
        public Object getregist(@RequestParam(required = true, value = "name") String name, @RequestParam(required = true, value = "password") String password,@RequestParam(required = false, value = "sex") String sex,@RequestParam(required = false, value = "email") String email,@RequestParam(required = true, value = "phone") String phone){
            HttpHeaders headers = new HttpHeaders();
            JSONObject data =new JSONObject();
            data.put("name",name);
            data.put("password",password);
            data.put("sex",sex);
            data.put("email",email);
            data.put("phone",phone);
            String jsonstring=data.toString();
            headers.setContentType(MediaType.valueOf("application/json; UTF-8"));
            HttpEntity<String> strEntity = new HttpEntity<String>(jsonstring,headers);

            return restTemplate.postForObject("http://admin-server/api/registserver",data, JSONObject.class);
    }

    @PostMapping("/getchangeuser")
    public Object getchangeuser(@RequestParam(required = true, value = "name") String name, @RequestParam(required = true, value = "newPassword") String newPassword,@RequestParam(required = true, value = "oldPassword") String oldPassword,@RequestParam(required = false, value = "email") String email,@RequestParam(required = true, value = "phone") String phone) {
        HttpHeaders headers = new HttpHeaders();
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("oldPassword", oldPassword);
        data.put("newPassword", newPassword);
        data.put("email", email);
        data.put("phone", phone);
        String jsonstring = data.toString();
        headers.setContentType(MediaType.valueOf("application/json; UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(jsonstring, headers);

        return restTemplate.postForObject("http://admin-server/api/changeserver", data, JSONObject.class);
    }
    
}
