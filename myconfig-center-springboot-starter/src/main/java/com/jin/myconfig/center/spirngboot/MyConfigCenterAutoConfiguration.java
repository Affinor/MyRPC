package com.jin.myconfig.center.spirngboot;

import com.jin.myconfig.center.spirngboot.annotation.EnableMyConfigCenterConfiguration;
import com.jin.myconfig.center.spirngboot.config.ZkConfigCenter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author wangjin
 */
@Configuration
@ConditionalOnBean(annotation = EnableMyConfigCenterConfiguration.class)
public class MyConfigCenterAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void init(){
        HikariDataSource dataSource = applicationContext.getBean(HikariDataSource.class);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        dataSource.copyStateTo(hikariConfig);
        ZkConfigCenter configCenter = new ZkConfigCenter();
        configCenter.init();
        configCenter.setConfig(dataSource,hikariConfig);
    }

}
