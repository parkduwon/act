package com.act.ldk.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 거래 봇 설정
 * Java 21 Virtual Thread 활용한 스케줄링
 */
@Configuration
@EnableScheduling
public class TradingBotConfiguration {
    
    @Bean
    @ConditionalOnProperty(
        value = "spring.threads.virtual.enabled", 
        havingValue = "true", 
        matchIfMissing = true
    )
    public ThreadPoolTaskScheduler virtualThreadScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("trading-bot-vt-");
        scheduler.setPoolSize(10);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setThreadFactory(Thread.ofVirtual().factory());
        scheduler.initialize();
        return scheduler;
    }
}