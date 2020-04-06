package com.jin.myconfig.center.springboot.myconfigcenterspringboottest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class MyconfigCenterSpringbootTestApplicationTests {
    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() {
        System.out.println(dataSource);
    }
    @Test
    void tLoads() {
        String s = "/abc";
        String substring = s.substring(1);
        System.out.println(substring);
    }
}
