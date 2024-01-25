package com.ldw.shop.handle;

import com.ldw.shop.config.SpringBeanContext;
import com.ldw.shop.dao.pojo.GoodsCart;
import com.ldw.shop.service.CollectionGoodsService;
import com.ldw.shop.service.GoodsCartService;
import com.ldw.shop.service.impl.GoodsCartServiceIpml;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收藏 定时任务
 */
@Slf4j
public class GoodsCartUpdateTask extends QuartzJobBean {

//    @Autowired
//    private GoodsCartService goodsCartService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 定时任务
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("用户更新购物车_数据库持久化-------- {}", sdf.format(new Date()));
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        log.info("商品id: "+jobDataMap.getLong("rediskey"));

        // 使用工具类，根据类型获取容器中的bean
        GoodsCartServiceIpml goodsCartServiceIpml = (GoodsCartServiceIpml) SpringBeanContext.getBean(GoodsCartServiceIpml.class);
        goodsCartServiceIpml.updateGoodsCart(jobDataMap.getLong("rediskey"),jobDataMap.getInt("userid"));
    }
}
