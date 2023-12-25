package com.ldw.shop.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ShopViewThreadUtils {
/**
    //期望此操作在线程池执行，不会影响原有的主线程
    @Async("taskExecutor") //使用线程池只需要添加次注解即可  申明该方法是一个异步任务
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        //在这里写入实际的文章阅读数量的操作
        int viewCounts = article.getViewCounts();
        Article articleupdate = new Article();
        articleupdate.setViewCounts(viewCounts+1);
        LambdaQueryWrapper<Article> updatewrapper=new LambdaQueryWrapper<>();
        updatewrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线成的环境下 线程安全：
        updatewrapper.eq(Article::getViewCounts,viewCounts);
//        update article set view_count=100 where  view_count=99 and id =11
        articleMapper.update(articleupdate,updatewrapper);

        try {
            Thread.sleep(5000);//睡眠5秒
            System.out.println("更新完成...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    **/
}
