package com.jin.myconfig.center.spirngboot.annotation;

import java.lang.annotation.*;

/**
 * @author wangjin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSource {
    String name() default "";
}
