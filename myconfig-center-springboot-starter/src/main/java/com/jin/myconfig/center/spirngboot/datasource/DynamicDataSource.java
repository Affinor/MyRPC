package com.jin.myconfig.center.spirngboot.datasource;

import com.jin.myconfig.center.spirngboot.config.ZkConfigCenter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.util.DriverDataSource;
import com.zaxxer.hikari.util.UtilityElf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Component
@Configuration
public class DynamicDataSource implements ApplicationContextAware, ApplicationListener {
    @Value("${spring.datasource.driver-class-name}")
    private String dcname;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
    private ApplicationContext app;
    @Override
    public void setApplicationContext(ApplicationContext app) throws BeansException {
        this.app = app;
    }

    @EventListener
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //如果是容器刷新事件  
        if(event instanceof MyApplicationEvent) {
            regDynamicBean();
        }
    }
    private void regDynamicBean() {
        // 解析属性文件，得到数据源Map
        Map<String, DataSourceInfo> mapCustom = parsePropertiesFile();
        // 把数据源bean注册到容器中
        addSourceBeanToApp(mapCustom);

    }
    /**
     * 直接通过反射技术，修改dataSource
     * @param mapCustom
     */
    private void addSourceBeanToApp(Map<String, DataSourceInfo> mapCustom) {
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) app.getAutowireCapableBeanFactory();
        acf.setAllowBeanDefinitionOverriding(true);
        BeanDefinition beanDefinition;
        Iterator<String> iter = mapCustom.keySet().iterator();
        while(iter.hasNext()){
            String beanKey = iter.next();
            HikariDataSource hikariDataSource = (HikariDataSource)app.getBean("dataSource");
            try {
                Field sealed = hikariDataSource.getClass().getSuperclass().getDeclaredField("sealed");
                sealed.setAccessible(true);
                sealed.set(hikariDataSource,false);

                hikariDataSource.setJdbcUrl(mapCustom.get(beanKey).getConnUrl());
                hikariDataSource.setUsername(mapCustom.get(beanKey).getUserName());
                hikariDataSource.setPassword(mapCustom.get(beanKey).getPassword());
                hikariDataSource.setPoolName("my-datasource");

                Field pool = hikariDataSource.getClass().getDeclaredField("pool");
                pool.setAccessible(true);
                HikariPool hikariPool = (HikariPool)pool.get(hikariDataSource);
                Field dataSource = hikariPool.getClass().getSuperclass().getDeclaredField("dataSource");
                dataSource.setAccessible(true);
                Properties properties = new Properties();
                properties.setProperty("password",hikariDataSource.getPassword());
                properties.setProperty("user",hikariDataSource.getUsername());
                DriverDataSource driverDataSource = new DriverDataSource(hikariDataSource.getJdbcUrl(),dcname,properties,hikariDataSource.getUsername(),hikariDataSource.getPassword());
                hikariDataSource.setDataSource(driverDataSource);
                dataSource.set(hikariPool,driverDataSource);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    /**
     * 功能说明：解析属性文件，得到数据源Map
     * @return
     * @throws IOException
     */
    private Map<String, DataSourceInfo> parsePropertiesFile() {

        Map<String, DataSourceInfo> mapDataSource = new HashMap<String,DataSourceInfo>();
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        dataSourceInfo.setConnUrl(url);
        dataSourceInfo.setUserName(username);
        dataSourceInfo.setPassword(password);
        ZkConfigCenter.center.forEach((k,v)->{
            if ("/spring.datasource.url".equals(k)){
                dataSourceInfo.setConnUrl(v);
            }
            if ("/spring.datasource.username".equals(k)){
                dataSourceInfo.setUserName(v);
            }
            if ("/spring.datasource.password".equals(k)){
                dataSourceInfo.setPassword(v);
            }
        });
        mapDataSource.put("dataSource",dataSourceInfo);
        return mapDataSource;
    }

}


