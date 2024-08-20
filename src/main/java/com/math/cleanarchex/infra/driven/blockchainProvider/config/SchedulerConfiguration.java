package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.NodeSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
@EnableAsync
@EnableRetry
@Slf4j
public class SchedulerConfiguration implements SchedulingConfigurer {

    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledExecutorService);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler(NodeSettings nodeSettings) {
        scheduledExecutorService = Executors.newScheduledThreadPool(nodeSettings.getNodes().size(), new CustomizableThreadFactory("eventeum-scheduler"));
        return scheduledExecutorService;
    }
}
