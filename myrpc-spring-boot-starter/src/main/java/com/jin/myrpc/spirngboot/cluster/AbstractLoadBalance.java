package com.jin.myrpc.spirngboot.cluster;

import com.jin.myrpc.spirngboot.registry.URL;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangjin
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public URL select(List<URL> services) {
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }
        if (services.size() == 1) {
            return services.get(0);
        }
        return doSelect(services);
    }

    protected abstract URL doSelect(List<URL> services);
}
