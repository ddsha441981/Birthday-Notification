package com.cwc.birthday.notification.utils.schedulers;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import com.cwc.birthday.notification.service.NotificationServiceAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

    private final NotificationServiceAlert notificationServiceAlert;
    private final BirthdayService birthdayService;

    public NotificationScheduler(NotificationServiceAlert notificationServiceAlert, BirthdayService birthdayService) {
        this.notificationServiceAlert = notificationServiceAlert;
        this.birthdayService = birthdayService;
    }

    /**
     * Runs daily at 11:59 AM
     * */
    @Scheduled(cron = "59 11 * * * *")
    //@Scheduled(cron = "0 * * * * *")//Run every min for testing purpose
    public void start() {
        try {
            List<Birthday> birthdays = birthdayService.getTodayBirthdays();
            birthdays.forEach(birthday ->
                    notificationServiceAlert.notifyNotification(() -> "Success", birthday)
            );
        } catch (Exception e) {
            log.error("Notification failed: {}", e.getMessage(), e);
        }
    }

}
