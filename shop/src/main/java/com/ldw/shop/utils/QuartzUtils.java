package com.ldw.shop.utils;

import com.ldw.shop.handle.CollectionSaveTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static org.quartz.DateBuilder.futureDate;

/**
 * 开启定时任务持久化存储到数据库
 */
public class QuartzUtils {

    //全局唯一，任务名称
    private String identyty;
    //Scheduler实例的工厂类
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    //创建调度器
    private Scheduler scheduler= getScheduler();
    //构造器
    public QuartzUtils(String identyty){
        this.identyty=identyty;
    }
    //获取Scheduler对象
    private static Scheduler getScheduler() {
        try {
            return schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    //启动任务
    public void startScheduTask(QuartzJobBean s,Long goodsid,Integer userid) throws SchedulerException{
        /**
         * 1.先判断当前任务是否在启动
         * 否：说明是首次启动定时任务，直接调用启动任务
         * 是：说明当前有任务正在执行，先把之前的任务删除之后，再重新初始化任务，使定时器刷新
         * 比如：当第一个请求过来的时候启动定时任务，在该任务执行过程中，其他的请求对该任务进行修改了，那么就要将该任务刷新，重新定时
         */
        if(!scheduler.isStarted()){
            scheduler.scheduleJob(jobDetail(s), trigger(s,goodsid,userid));
            scheduler.start();
        }else{
            TriggerKey triggerKey = TriggerKey.triggerKey(identyty);
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(identyty));

            scheduler.scheduleJob(jobDetail(s), trigger(s,goodsid,userid));
            scheduler.start();
        }
    }

    //启动任务
    public void startScheduTask(QuartzJobBean s,Integer userid) throws SchedulerException{
        /**
         * 1.先判断当前任务是否在启动
         * 否：说明是首次启动定时任务，直接调用启动任务
         * 是：说明当前有任务正在执行，先把之前的任务删除之后，再重新初始化任务，使定时器刷新
         * 比如：当第一个请求过来的时候启动定时任务，在该任务执行过程中，其他的请求对该任务进行修改了，那么就要将该任务刷新，重新定时
         */
        if(!scheduler.isStarted()){
            scheduler.scheduleJob(jobDetail(s), trigger(s,userid));
            scheduler.start();
        }else{
            TriggerKey triggerKey = TriggerKey.triggerKey(identyty);
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(identyty));

            scheduler.scheduleJob(jobDetail(s), trigger(s,userid));
            scheduler.start();
        }
    }

    //  创建任务 JobDetail 定时任务内容
    public JobDetail jobDetail(QuartzJobBean s){
        //newJob - 组名 - 任务名称 - 是否持久化
        //withIdentity - 全局唯一的 id - 任务名称
        return JobBuilder.newJob(s.getClass()) //这里要改
                .withIdentity(identyty)
                .build();
    }
    //  创建 Trigger 触发器 定时任务内容
    public Trigger trigger(QuartzJobBean s,Long goodsid,Integer userid){
        // 使用秒  SimpleScheduleBuilder
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .repeatSecondlyForever(5)//任务开始5秒后执行
                .withRepeatCount(0);//只执行一次，不重复
        //forJob 设置触发的对象
        return TriggerBuilder.newTrigger().forJob(jobDetail(s))
                .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))//10秒以后开始触发
                .withSchedule(simpleScheduleBuilder)
                .usingJobData("userid",userid)
                .usingJobData("rediskey",goodsid)
                .build();
    }

    //  创建 Trigger 触发器 定时任务内容
    public Trigger trigger(QuartzJobBean s,Integer userid){
        // 使用秒  SimpleScheduleBuilder
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .repeatSecondlyForever(5)//任务开始5秒后执行
                .withRepeatCount(0);//只执行一次，不重复
        //forJob 设置触发的对象
        return TriggerBuilder.newTrigger().forJob(jobDetail(s))
                .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))//10秒以后开始触发
                .withSchedule(simpleScheduleBuilder)
                .usingJobData("userid",userid)
                .build();
    }

}
