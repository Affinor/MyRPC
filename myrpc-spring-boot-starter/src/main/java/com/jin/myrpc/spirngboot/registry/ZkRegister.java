package com.jin.myrpc.spirngboot.registry;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.INITIALIZED;

/**
 * @author wangjin
 */
public class ZkRegister implements Register{

    private static Map<String,List<URL>> remoteServices = new ConcurrentHashMap<>();

    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    private CuratorFramework curatorFramework = buildCuratorFramework();

    public CuratorFramework buildCuratorFramework(){
        try {
            CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                    .connectString(InetAddress.getLocalHost().getHostAddress() + ":" + "2181")
                    .retryPolicy(buildRetryPolicy())
                    .namespace("myrpc_service")
                    .sessionTimeoutMs(2100)
                    .build();
            curatorFramework.start();
            curatorFramework.blockUntilConnected(1, TimeUnit.SECONDS);
            final PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, "/", false);
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            childrenCache.getListenable().addListener(
                    new PathChildrenCacheListener() {
                        @Override
                        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                                throws Exception {
                            if (!event.getType().equals(INITIALIZED)){
                                remoteServices.remove(event.getData().getPath());
                                System.out.println("子节点发生变化，删除本地缓存："+event.getData().getPath());
                            }
                        }
                    },
                    pool
            );
            return curatorFramework;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public RetryPolicy buildRetryPolicy() {
        int baseSleepTimeMs = 50;
        int maxRetries = 10;
        int getMaxSleepMs = 500;
        return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, getMaxSleepMs);
    }
    public void doRegister(URL url,String k){
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/"+k+"/"+ url.getServerAddress() + ":"+url.getServerPort(),"1".getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void register(URL url) {
        RegistryHandler.registryMap.forEach((k,v)->{
            try {
                doRegister(url,k);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unregister(String url) {

    }

    @Override
    public List<URL> lookup(String serviceName) {
        List<URL> urlList = new ArrayList<>();
        try {
            //如果本地缓存中有，就直接从本地缓存中取
            if (remoteServices.containsKey(serviceName)){
                return remoteServices.get(serviceName);
            }
            //如果本地缓存中没有，就去zookeeper中查
            List<String> strings = curatorFramework.getChildren().forPath(serviceName);
            if (!CollectionUtils.isEmpty(strings)){
                strings.forEach(s -> {
                    String[] split = s.split(":");
                    URL url = new URL();
                    url.setServerAddress(split[0]);
                    url.setServerPort(split[1]);
                    urlList.add(url);
                });
            }
            remoteServices.put(serviceName,urlList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return urlList;
    }
}
