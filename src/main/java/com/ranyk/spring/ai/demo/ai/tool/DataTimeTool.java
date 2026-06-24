package com.ranyk.spring.ai.demo.ai.tool;

import com.ranyk.spring.ai.demo.service.DelayedTaskService;
import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledFuture;

/**
 * CLASS_NAME: DataTimeTool.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Spring AI 调用的时间处理工具类
 * @date: 2026-06-23
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class DataTimeTool {

    /**
     * 延迟任务服务对象
     */
    private final DelayedTaskService delayedTaskService;

    /**
     * 构造方法 - 向 Spring IOC 容器中注册 DataTimeTool 类型的 Bean , 同时通过 Spring IOC 的 DI 依赖注入 DelayedTaskService 类型的 Bean
     *
     * @param delayedTaskService 延迟任务服务对象
     */
    @Autowired
    public DataTimeTool(DelayedTaskService delayedTaskService) {
        this.delayedTaskService = delayedTaskService;
    }

    /**
     * 获取当前时间的工具方法, 获取用户所在时区的当前时间
     *
     * @return 返回用户所在时区的当前时间(请求响应时的实时时间), 格式为 yyyy-MM-dd HH:mm:ss
     */
    @Tool(description = "获取用户在指定时区的当前日期和时间，用于回答需要实时时间的问题")
    public String getCurrentTime(){
        // 获取用户的时区偏好设置
        var zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        var now = LocalDateTime.now().atZone(zoneId);
        // 格式化为 yyyy-MM-dd HH:mm:ss
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
    }

    /**
     * 设置闹钟工具方法, 根据用户输入的闹钟时间, 在指定时间触发提醒
     *
     * @param alarmTime 闹钟时间，格式为 yyyy-MM-dd HH:mm:ss
     */
    @Tool(description = "设置闹钟，调用此工具可在指定时间触发提醒。时间参数必须是 ISO-8601 格式，例如：2026-05-03 15:30:00")
    public void setAlarm(@ToolParam(description = "闹钟的触发时间，标准格式：yyyy-MM-dd HH:mm:ss") String alarmTime) {
        log.info("⏰ 闹钟已设置，将在 {} 提醒用户。", alarmTime);
        // 此处可扩展实际逻辑：如存入数据库、发送推送通知等, 当前处理为, 创建一个异步线程, 在器到点时间时, 触发任务执行
        // 获取 alarmTime 的毫秒戳
        var zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        var alarmTimeMillis = LocalDateTime.parse(alarmTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(zoneId).toInstant().toEpochMilli();
        log.info("创建一个 [ 虚拟异步线程 - 闹钟 ] , 在指定时间 {} 触发任务执行.", alarmTimeMillis);
        // 创建一个异步线程, 在指定时间时, 触发任务执行
        ScheduledFuture<?> scheduledFuture = delayedTaskService.executeAtTime(() -> {
            log.info("[ 虚拟异步线程 - 闹钟 ] 定时任务执行, 当前时间: {}", LocalDateTime.now());
            // 音频文件的 URL
            String audioUrl = "https://er-sycdn.kuwo.cn/4033fdbeacde1e96ea43981122ab9599/6a3b424c/resource/30106/trackmedia/M500004Ho1IB2fmXmT.mp3";
            try (BufferedInputStream bis = new BufferedInputStream(new URI(audioUrl).toURL().openStream())) {
                Player player = new Player(bis);
                player.play(); // 阻塞线程直到播放结束
            } catch (Exception e) {
                log.error("❌ 闹钟音频播放失败: {}", e.getMessage(), e);
            }
        }, alarmTimeMillis);
    }
}
