package com.jin.myrpc.spirngboot.annotation;

import java.lang.annotation.*;

/**
 * @author wangjin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MyService {
    Class<?> interfaceClass();
}
