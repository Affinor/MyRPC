package com.jin.myrpc.spirngboot;

import com.jin.myrpc.spirngboot.annotation.EnableMyRPCConfiguration;
import com.jin.myrpc.spirngboot.annotation.MyReference;
import com.jin.myrpc.spirngboot.consumer.RpcProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * @author wangjin
 */
@Configuration
@ConditionalOnClass(MyReference.class)
@ConditionalOnBean(annotation = EnableMyRPCConfiguration.class)
@AutoConfigureAfter(MyRPCAutoConfiguration.class)
public class MyRPCConsumerAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class<?> targetCls = bean.getClass();
                Field[] targetFld = targetCls.getDeclaredFields();
                for (Field field : targetFld) {
                    //找到制定目标的注解类
                    if (field.isAnnotationPresent(MyReference.class)) {
                        if (!field.getType().isInterface()) {
                            throw new BeanCreationException("RoutingInjected field must be declared as an interface:" + field.getName()
                                    + " @Class " + targetCls.getName());
                        }
                        try {
                            this.handleRoutingInjected(field, bean, field.getType());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bean;
            }
            /**
             * @param field
             * @param bean
             * @param type
             * @throws IllegalAccessException
             */
            private void handleRoutingInjected(Field field, Object bean, Class type) throws IllegalAccessException {
                field.setAccessible(true);
                Object proxy = RpcProxy.create(type);
                field.set(bean, proxy);
            }
        };
    }

}
