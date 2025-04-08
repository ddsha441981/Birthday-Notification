package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.model.NotificationLog;
import com.cwc.birthday.notification.repository.NotificationLogRepository;
import com.cwc.birthday.notification.service.NotificationServiceAlert;
import com.cwc.birthday.notification.utils.message.MessagePicker;
import com.cwc.birthday.notification.utils.notifications.EmailNotification;
import com.cwc.birthday.notification.utils.notifications.PushNotification;
import com.cwc.birthday.notification.utils.notifications.SmsNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class NotificationAlertServiceImpl implements NotificationServiceAlert {
    private final Logger log = LoggerFactory.getLogger(NotificationAlertServiceImpl.class);

    private final EmailNotification emailNotification;
    private final SmsNotification smsNotification;
    private final PushNotification pushNotification;
    private final MessagePicker messagePicker;
    private final NotificationLogRepository logRepository;


    public NotificationAlertServiceImpl(EmailNotification emailNotification, SmsNotification smsNotification, PushNotification pushNotification, MessagePicker messagePicker, NotificationLogRepository logRepository) {
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
        this.pushNotification = pushNotification;
        this.messagePicker = messagePicker;
        this.logRepository = logRepository;
    }


    /**
     * Sends personalized notifications (Email, SMS, and Push) for various occasions such as
     * Birthdays, Anniversaries, and Festivals.
     * <p>
     * Based on the event type and name, a random message is picked from a corresponding
     * message file (e.g., birthday_messages.txt, anniversary.txt, or festival-[event].txt),
     * and delivered through available communication channels. Each channel (Email, SMS, Push)
     * is triggered only if the relevant contact information is present.
     * </p>
     *
     * @param supplier a generic supplier used for returning a custom result (useful for callbacks or logging)
     * @param birthday the {@link Birthday} object containing recipient's details like name, email, contact number,
     *                 device token, event type (Birthday, Anniversary, Festival), and optionally event name (e.g., Diwali)
     * @param <T>      the type of result returned by the supplier
     * @return the result provided by the supplier
     *
     * @implNote Ensure that the Excel file includes accurate and up-to-date values for all necessary fields:
     *           - Event type (e.g., "Birthday", "Anniversary", "Festival")
     *           - Event name (only for festivals, e.g., "Diwali", "Holi")
     *           - Email, Contact Number, and Device Token
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
        String eventType = birthday.getMessageType();
        String eventName = birthday.getEventName();
        LocalDate today = LocalDate.now();


        // Check for duplicates
        if (logRepository.existsByEmailAndEventTypeAndEventNameAndSentDate(email, eventType, eventName, today)) {
            log.info("⏩ Notification already sent today to {} for {} - {}.", name, eventType, eventName);
            return supplier.get();
        }

        String message = messagePicker.pickRandomMessage(
                birthday.getMessageType(), birthday.getEventName()
        );

        String subject = "🎉 " + birthday.getMessageType() + " Wishes for " + birthday.getName();
        String htmlMessageFormat = "<h2>Dear " + birthday.getName() + ",</h2><p>" + message + "</p>";

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
                            try {
                                pushNotification.sendPushNotification(token, subject, message);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            log.info("✅ Push notification sent to {}", name);
                        },
                        () -> log.warn("⚠️ Push not sent: device token is missing for {}", name)
                );


        // Log the notification
        logRepository.save(new NotificationLog(email, contactNumber, eventType, eventName, today));

        return supplier.get();
    }

}
