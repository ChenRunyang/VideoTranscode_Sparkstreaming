package com.cry.videotransform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.cry.videotransform.Dao")
@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
public class VideotransformApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideotransformApplication.class, args);
    }

}
