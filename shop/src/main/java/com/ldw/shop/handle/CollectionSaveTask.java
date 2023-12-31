package com.ldw.shop.handle;


import com.ldw.shop.service.CollectionGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.DateBuilder.futureDate;

/**
 * 收藏 定时任务
 */
@Slf4j
public class CollectionSaveTask extends QuartzJobBean{

    @Autowired
    private CollectionGoodsService collectionGoodsService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 定时任务
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("用户商品收藏_数据库持久化-------- {}", sdf.format(new Date()));

//        try {
//             collectionGoodsService.     // 具体任务
//        } catch (Exception e) {
//            logger.error("任务失败 error:{}", e.getMessage());
//        }

        //将 Redis 里的点赞信息同步到数据库里
//        dbService.transLikedFromRedis2DB();
//        dbService.transLikedCountFromRedis2DB();
    }






}
