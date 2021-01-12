package com.cry.videotransform.service;

import javax.annotation.PostConstruct;

public class ComsumerService {
    private static ComsumerService comsumerService;

    @PostConstruct
    public void init(){
        comsumerService=this;
    }

}
