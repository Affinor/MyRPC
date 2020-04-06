package com.jin.myconfig.center.spirngboot.config;

import com.alibaba.fastjson.JSONObject;
import com.jin.myconfig.center.spirngboot.utils.CuratorUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author wangjin
 */
@Configuration
public class ZkConfigCenter implements MyConfigCenter {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Qualifier(value = "dataSource")
    @Resource
    HikariDataSource hikariDataSource;

    @Bean
    public ExecutorService childrenListenerPool(){
        return Executors.newFixedThreadPool(1);
    }

    private CuratorFramework curatorFramework = CuratorUtil.buildCuratorFramework();
    public static Map<String,String> center = new ConcurrentHashMap<>();
    @Qualifier(value = "childrenListenerPool")
    @Resource
    private ExecutorService childrenListenerPool;
    @Override
    @PostConstruct
    public void init() {
        try {
            final PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, "/", false);
            childrenCache.start(PathChildrenCache.StartMode.NORMAL);
            childrenCache.getListenable().addListener(
                    new PathChildrenCacheListener() {
                        @Override
                        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                                throws Exception {
                            HikariConfig hikariConfig = new HikariConfig();

                            String path = event.getData().getPath();
                            String data = new String(curatorFramework.getData().forPath(path),"UTF-8");
                            if (StringUtils.isEmpty(path) || StringUtils.isEmpty(data)){
                                return;
                            }
                            center.put(path,data);

                        }
                    },
                    childrenListenerPool
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getConfig(Class<?> clazz) {
        return null;
    }

    @Override
    public void setConfig(Class<?> clazz, Map<String, String> config, ApplicationContext applicationContext) {

    }

    public void setConfig(HikariDataSource dataSource, HikariConfig hikariConfig) {
        try {
            byte[] urlbytes = curatorFramework.getData().watched().forPath("/spring.datasource.url");
            if (null!=urlbytes){
                String url = new String(urlbytes, "UTF-8");
                hikariConfig.setJdbcUrl(url);
            }
            byte[] usernamebytes = curatorFramework.getData().watched().forPath("/spring.datasource.username");
            if (null!=urlbytes){
                String username = new String(usernamebytes, "UTF-8");
                hikariConfig.setJdbcUrl(username);
            }
            byte[] passwordbytes = curatorFramework.getData().watched().forPath("/spring.datasource.password");
            if (null!=passwordbytes){
                String password = new String(passwordbytes, "UTF-8");
                hikariConfig.setJdbcUrl(password);
            }
            dataSource.copyStateTo(hikariConfig);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
