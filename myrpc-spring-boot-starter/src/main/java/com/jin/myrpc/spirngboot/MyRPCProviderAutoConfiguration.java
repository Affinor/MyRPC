package com.jin.myrpc.spirngboot;

import com.jin.myrpc.spirngboot.annotation.EnableMyRPCConfiguration;
import com.jin.myrpc.spirngboot.annotation.MyService;
import com.jin.myrpc.spirngboot.registry.RegistryHandler;
import com.jin.myrpc.spirngboot.registry.RpcRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangjin
 */
@Configuration
@ConditionalOnClass(MyService.class)
@ConditionalOnBean(annotation = EnableMyRPCConfiguration.class)
@AutoConfigureAfter(MyRPCAutoConfiguration.class)
public class MyRPCProviderAutoConfiguration {

    //创建线程池对象
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beanMap = this.applicationContext.getBeansWithAnnotation(MyService.class);
        if (beanMap != null && beanMap.size() > 0) {
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                this.initProviderBean(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 保存服务
     * @param key
     * @param value
     */
    private void initProviderBean(String key, Object value) {
        MyService annotation = value.getClass().getAnnotation(MyService.class);
        String name = annotation.interfaceClass().getName();
        RegistryHandler.registryMap.put(key,value);
        RegistryHandler.registryMap.put(name,value);
        executor.submit(new RpcRegistry(8888));
        System.out.println("myrpc start:8888");
    }

}
