package com.jin.myrpc.spirngboot.protocol;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author wangjin
 */
@Data
@ToString
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 4852686539770738744L;
    /**
     * 请求对象的ID
     */
    private String requestId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 入参
     */
    private Object[] parameters;

}
