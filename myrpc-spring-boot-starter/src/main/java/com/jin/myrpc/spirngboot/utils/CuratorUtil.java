package com.jin.myrpc.spirngboot.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjin
 */
public class CuratorUtil {
    public static CuratorFramework buildCuratorFramework(){
        CuratorFramework curatorFramework = null;
        try {
            curatorFramework = CuratorFrameworkFactory.builder()
                    .connectString(InetAddress.getLocalHost().getHostAddress() + ":" + "2181")
                    .retryPolicy(buildRetryPolicy())
                    .namespace("myrpc_service")
                    .sessionTimeoutMs(2100)
                    .build();
            curatorFramework.start();
            curatorFramework.blockUntilConnected(1, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return curatorFramework;
    }

    public static RetryPolicy buildRetryPolicy() {
        int baseSleepTimeMs = 50;
        int maxRetries = 10;
        int getMaxSleepMs = 500;
        return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, getMaxSleepMs);
    }
}
