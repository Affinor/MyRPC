package com.jin.myrpc.spirngboot.cluster;

import com.jin.myrpc.spirngboot.registry.URL;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**随机
 * @author wangjin
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    protected URL doSelect(List<URL> services) {
        return services.get(ThreadLocalRandom.current().nextInt(services.size()));
    }
}
