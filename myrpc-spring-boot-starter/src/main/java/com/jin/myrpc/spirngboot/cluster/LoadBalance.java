package com.jin.myrpc.spirngboot.cluster;

import com.jin.myrpc.spirngboot.registry.URL;

import java.util.List;

/**
 * @author wangjin
 */
public interface LoadBalance {
    URL select(List<URL> services);
}
