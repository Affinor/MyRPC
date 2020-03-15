package com.jin.myrpc.myrpcprovidertest;

import com.jin.myrpc.spirngboot.annotation.EnableMyRPCConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMyRPCConfiguration
@SpringBootApplication
public class MyrpcProviderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyrpcProviderTestApplication.class, args);
    }

}
