package com.jin.myconfig.center.spirngboot.annotation;

import java.lang.annotation.*;

/**
 * @author wangjin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMyConfigCenterConfiguration {
}
