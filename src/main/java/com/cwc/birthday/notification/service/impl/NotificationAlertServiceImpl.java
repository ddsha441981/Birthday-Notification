package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.NotificationServiceAlert;
import com.cwc.birthday.notification.utils.message.BirthdayMessagePicker;
import com.cwc.birthday.notification.utils.notifications.EmailNotification;
import com.cwc.birthday.notification.utils.notifications.PushNotification;
import com.cwc.birthday.notification.utils.notifications.SmsNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class NotificationAlertServiceImpl implements NotificationServiceAlert {
    private final Logger log = LoggerFactory.getLogger(NotificationAlertServiceImpl.class);
    private final EmailNotification emailNotification;
    private final SmsNotification smsNotification;
    private final PushNotification pushNotification;
    private final BirthdayMessagePicker birthdayMessagePicker;


    public NotificationAlertServiceImpl(EmailNotification emailNotification, SmsNotification smsNotification, PushNotification pushNotification, BirthdayMessagePicker birthdayMessagePicker) {
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
        this.pushNotification = pushNotification;
        this.birthdayMessagePicker = birthdayMessagePicker;
    }


    /**
     * Triggers birthday notifications via multiple channels: Email, SMS, and Push.
     * <p>
     * This method picks a random birthday message and sends it to the provided user
     * using available communication channels. Each channel is invoked only if the
     * corresponding data (email, phone number, device token) is present and valid.
     * </p>
     *
     * @param supplier a generic supplier that provides a return value (useful for logging or callbacks)
     * @param birthday the {@link Birthday} object containing user details like name, email, phone, and device token
     * @param <T>      the type of object returned by the supplier
     * @return the result provided by the supplier
     *
     * @implNote Ensure that the device token, email, and contact number are properly stored
     *           and retrieved before triggering this method to avoid skipped notifications.
     *
     * @see java.util.function.Supplier
     * @see com.cwc.birthday.notification.model.Birthday
     */
    @Override
    public <T> T notifyNotification(Supplier<T> supplier, Birthday birthday) {
        //TODO: Need to implement device token and manage it excel file also if you need get push notification
        String deviceToken = birthday.getDeviceToken();
        String name = birthday.getName();
        String email = birthday.getEmail();
        String contactNumber = birthday.getContactNumber();
        String message = birthdayMessagePicker.getRandomBirthdayMessage();

        String subject = "🎂 Happy Birthday " + name + "!";
        String htmlMessageFormat = "<h2>Dear " + name + ",</h2><p>" + message + "</p>";

        // TODO: Email Notification
        Optional.ofNullable(email)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        e -> {
                            emailNotification.sendEmail(e, subject, htmlMessageFormat, true);
                            log.info("✅ Email sent to {}", e);
                        },
                        () -> log.warn("⚠️ Email not sent: email is missing for {}", name)
                );

        // TODO: SMS Notification
        Optional.ofNullable(contactNumber)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        number -> {
                            smsNotification.sendSms(number, message);
                            log.info("✅ SMS sent to {}", number);
                        },
                        () -> log.warn("⚠️ SMS not sent: contact number is missing for {}", name)
                );

        // TODO: Push Notification
        Optional.ofNullable(deviceToken)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        token -> {
                            pushNotification.sendPushNotification(token, subject, message);
                            log.info("✅ Push notification sent to {}", name);
                        },
                        () -> log.warn("⚠️ Push not sent: device token is missing for {}", name)
                );
        return supplier.get();
    }

}
