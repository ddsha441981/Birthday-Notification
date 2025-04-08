package com.cwc.birthday.notification.utils.schedulers;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import com.cwc.birthday.notification.service.NotificationServiceAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class NotificationScheduler {
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private static final int BATCH_SIZE = 1000;
    private final Semaphore semaphore = new Semaphore(50);

    private final NotificationServiceAlert notificationServiceAlert;
    private final BirthdayService birthdayService;

    public NotificationScheduler(NotificationServiceAlert notificationServiceAlert, BirthdayService birthdayService) {
        this.notificationServiceAlert = notificationServiceAlert;
        this.birthdayService = birthdayService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run daily at midnight
//    @Scheduled(cron = "0 * * * * *")
    public void start() {
        try {
            log.info("Starting birthday notification process...");
            processBirthdaysInBatches();
            log.info("Birthday notification process scheduled.");
        } catch (Exception e) {
            log.error("Failed to schedule birthday notifications: {}", e.getMessage(), e);
        }
    }

    private void processBirthdaysInBatches() {
        int pageNumber = 0;
        Page<Birthday> birthdayPage;

        do {
            Pageable pageable = PageRequest.of(pageNumber++, BATCH_SIZE);
            birthdayPage = birthdayService.getTodayBirthdays(pageable);

            birthdayPage.getContent().forEach(this::processBirthdayAsync);
        } while (birthdayPage.hasNext());
    }


    @Async("notificationTaskExecutor")
    public void processBirthdayAsync(Birthday birthday) {
        try {
            semaphore.acquire();
            notificationServiceAlert.notifyNotification(() -> "Success", birthday);
            log.debug("Processed notification for {}", birthday.getName());
        } catch (Exception e) {
            log.error("Failed to process notification for {}: {}", birthday.getName(), e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}
