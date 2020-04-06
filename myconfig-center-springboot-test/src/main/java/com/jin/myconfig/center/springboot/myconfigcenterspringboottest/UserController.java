package com.jin.myconfig.center.springboot.myconfigcenterspringboottest;

import com.alibaba.fastjson.JSONObject;
import com.jin.myconfig.center.spirngboot.datasource.MyApplicationEvent;
import com.jin.myconfig.center.spirngboot.utils.CuratorUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * @author wangjin
 */
@RestController
public class UserController {
    @Autowired
    private ApplicationContext app;
    @Autowired
    HikariDataSource hikariDataSource;
    @Autowired
    DataSource dataSource;
    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public String getUsers(){
        return JSONObject.toJSONString(userService.getUsers());
    }

    @GetMapping("/setUserName")
    public String getUsers(String username){
        try {
            CuratorFramework curatorFramework = CuratorUtil.buildCuratorFramework();
            if (null==curatorFramework.checkExists().forPath("/spring.datasource.username")) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath("/spring.datasource.username");
            }
            curatorFramework.setData().forPath("/spring.datasource.username",username.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        app.publishEvent(new MyApplicationEvent(app));
        System.out.println(dataSource.toString());
        return hikariDataSource.getUsername()+":"+hikariDataSource.getPassword();
    }

    @GetMapping("/setUrl")
    public String setUrl(String url){
        try {
            CuratorFramework curatorFramework = CuratorUtil.buildCuratorFramework();
            if (null==curatorFramework.checkExists().forPath("/spring.datasource.url")) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath("/spring.datasource.url");
            }
            curatorFramework.setData().forPath("/spring.datasource.url",url.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        app.publishEvent(new MyApplicationEvent(app));
        System.out.println(dataSource.toString());
        return hikariDataSource.getUsername()+":"+hikariDataSource.getPassword();
    }
}
