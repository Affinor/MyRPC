package com.jin.myconfig.center.springboot.myconfigcenterspringboottest;

import com.jin.myconfig.center.spirngboot.annotation.EnableMyConfigCenterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMyConfigCenterConfiguration
@SpringBootApplication
public class MyconfigCenterSpringbootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyconfigCenterSpringbootTestApplication.class, args);
    }

}
