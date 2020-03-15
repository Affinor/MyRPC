package com.jin.myrpc.spirngboot.annotation;

import java.lang.annotation.*;

/**
 * @author wangjin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMyRPCConfiguration {
}
