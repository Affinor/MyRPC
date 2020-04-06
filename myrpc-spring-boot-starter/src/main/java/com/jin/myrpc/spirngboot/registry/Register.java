package com.jin.myrpc.spirngboot.registry;

import java.util.List;

/**
 * @author wangjin
 */
public interface Register {
    void register(URL url);
    void unregister(String url);
    List<URL> lookup(String serviceName);
}
