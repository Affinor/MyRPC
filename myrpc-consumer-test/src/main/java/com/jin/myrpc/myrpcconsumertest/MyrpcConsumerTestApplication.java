package com.jin.myrpc.myrpcconsumertest;

import com.jin.myrpc.spirngboot.annotation.EnableMyRPCConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMyRPCConfiguration
@SpringBootApplication
public class MyrpcConsumerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyrpcConsumerTestApplication.class, args);
    }

}
