package com.ldw.shop.handle;

import com.ldw.shop.common.constant.AbstractCache;
import com.ldw.shop.utils.ApplicationContextUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = {"cache.init.enable"}, havingValue = "true", matchIfMissing = false)
public class CachePreheatHandler implements CommandLineRunner {

    /**
     * 缓存预热
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        ApplicationContext context = ApplicationContextUtil.getContext();
        Map<String, AbstractCache> beansOfType = context.getBeansOfType(AbstractCache.class);
        for (Map.Entry<String, AbstractCache> cacheEntry : beansOfType.entrySet()) {
            AbstractCache cache = context.getBean(cacheEntry.getValue().getClass());
            cache.init();
        }
    }

}

