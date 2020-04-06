package com.jin.myconfig.center.spirngboot.config;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author wangjin
 */
public interface MyConfigCenter {
    /**
     * 配置中心初始化
     * @return
     */
    void init();

    /**
     * 获取配置
     * @param clazz
     * @return
     */
    Map<String,String> getConfig(Class<?> clazz);

    /**
     * 设置配置
     * @param clazz
     * @param config
     * @param applicationContext
     */
    void setConfig(Class<?> clazz, Map<String,String> config, ApplicationContext applicationContext);
}
