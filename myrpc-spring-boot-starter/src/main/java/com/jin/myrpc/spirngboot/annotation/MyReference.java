package com.jin.myrpc.spirngboot.annotation;

import java.lang.annotation.*;

/**
 * @author wangjin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MyReference {

}
