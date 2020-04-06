package com.jin.myrpc.spirngboot.cluster;

import com.jin.myrpc.spirngboot.registry.URL;
import com.jin.myrpc.spirngboot.utils.CuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**最少活跃
 * @author wangjin
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance{

    private CuratorFramework curatorFramework = CuratorUtil.buildCuratorFramework();

    @Override
    protected URL doSelect(List<URL> services) {
        Map<Long,URL> map = new HashMap<>(services.size());
        services.forEach(service->{
            try {
                String s = "/" + service.getServerAddress() + ":" + service.getServerPort();
                Stat stat = curatorFramework.checkExists().forPath(s);
                if (null == stat){
                    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(s, String.valueOf(TimeUnit.SECONDS.convert(System.currentTimeMillis(),TimeUnit.MILLISECONDS)).getBytes("UTF-8"));
                    System.out.println("首次记录，直接创建："+s);
                }else {
                    byte[] dataBytes = curatorFramework.getData().forPath(s);
                    if (null == dataBytes || dataBytes.length == 0) {
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(s, String.valueOf(TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS)).getBytes("UTF-8"));
                        System.out.println("首次记录，直接创建：" + s);
                    } else {
                        long last = Long.parseLong(new String(dataBytes, "UTF-8"));
                        long now = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                        if (now - last > 5) {
                            curatorFramework.setData().forPath(s, String.valueOf(TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS)).getBytes("UTF-8"));
                            System.out.println("超过5秒，直接清零：" + s);
                        } else {
                            map.put(now - last, service);
                            System.out.println("5秒之内，排序：" + s);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("打印map集合中的服务：");
        map.entrySet().stream().forEach(System.out::println);
        if (CollectionUtils.isEmpty(map)){
            return new RandomLoadBalance().select(services);
        }else if (map.size()==1){
            return map.values().stream().findFirst().get();
        }
        return map.entrySet().stream()
                .sorted(
                        Comparator.comparingLong(a-> -a.getKey())
                )
                .findFirst()
                .get()
                .getValue();
    }
}
