package com.xu.system.commons;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.common.pojo.Constants;
import com.xu.common.utils.WebSocket;
import com.xu.system.mapper.MyLogMapper;
import com.xu.system.pojo.MyLog;
import com.xu.system.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步工厂（产生任务用）
 *
 * @author AITIAN
 */
@Slf4j
@Component
public class AsyncManager  {

    @Resource
    private WebSocket webSocket;

    /**
     * 延时异步操作任务调度线程池
     */
    @Resource
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 异步操作任务调度线程池
     */
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private MyLogMapper myLogMapper;


    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        scheduledExecutorService.schedule(task, Constants.OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        threadPoolTaskExecutor.execute(task);
    }

    /**
     * 记录登录信息
     * @param user 用户
     * @param status   状态
     * @return 任务task
     */
    public TimerTask recordLoginInfo(User user, String status, String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                LocalDateTime localDateTime = LocalDateTime.now();
                MyLog recordedLog = myLogMapper.selectOne(new QueryWrapper<MyLog>().eq("user_name", user.getName()));
                if (recordedLog == null) {
                    MyLog userLog = new MyLog(null, user.getName(), ip, localDateTime, status);
                    myLogMapper.insert(userLog);
                    return;
                }
                recordedLog.setIp(ip);
                recordedLog.setStatus(status);
                recordedLog.setLastTime(localDateTime);
                myLogMapper.updateById(recordedLog);
            }
        };
    }

    /**
     * 评论 Socket 异步通知
     */
    public TimerTask newCommentNotice(String username, String content){
        return new TimerTask() {
            @Override
            public void run() {
                webSocket.sendOneMessage(username, content);
            }
        };
    }


    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍然超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    public void shutdown() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
            try {
                if (!scheduledExecutorService.awaitTermination(120, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                    if (!scheduledExecutorService.awaitTermination(120, TimeUnit.SECONDS)) {
                        log.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                scheduledExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

