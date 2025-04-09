package com.cwc.birthday.notification.config;

import com.cwc.birthday.notification.service.MessageSchedulerService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final  MessageSchedulerService schedulerService;

    public SchedulerConfig(MessageSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    // Run every minute to check for messages that need to be sent
    @Scheduled(fixedRate = 60000)
    public void processScheduledMessages() {
        schedulerService.processScheduledMessages();
    }
}
