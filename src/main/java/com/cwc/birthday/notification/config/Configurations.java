package com.cwc.birthday.notification.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class Configurations {

    @Bean(name = "notificationTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Notification-");
        executor.initialize();
        return executor;
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
