package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.model.NotificationLog;
import com.cwc.birthday.notification.repository.NotificationLogRepository;
import com.cwc.birthday.notification.service.NotificationServiceAlert;
import com.cwc.birthday.notification.utils.PhoneNumberUtil;
import com.cwc.birthday.notification.utils.message.MessagePicker;
import com.cwc.birthday.notification.utils.notifications.*;
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
    private final WhatsAppNotification whatsAppNotification;
    private final VoiceCallNotification voiceCallNotification;
    private final MessagePicker messagePicker;
    private final NotificationLogRepository logRepository;


    public NotificationAlertServiceImpl(EmailNotification emailNotification, SmsNotification smsNotification, PushNotification pushNotification, WhatsAppNotification whatsAppNotification, VoiceCallNotification voiceCallNotification, MessagePicker messagePicker, NotificationLogRepository logRepository) {
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
        this.pushNotification = pushNotification;
        this.whatsAppNotification = whatsAppNotification;
        this.voiceCallNotification = voiceCallNotification;
        this.messagePicker = messagePicker;
        this.logRepository = logRepository;
    }

    /**
     * Sends personalized notifications (Email, SMS, Push, WhatsApp, and Voice Call) for occasions such as Birthdays,
     * Anniversaries, and Festivals based on the provided event details.
     * <p>
     * This method selects a random message from a predefined set (e.g., birthday_messages.txt, anniversary.txt, or
     * festival-[event].txt) based on the event type and optional event name. Notifications are sent through available
     * channels (Email, SMS, Push, WhatsApp, Voice Call) only if the corresponding contact information (email, phone
     * number, or device token) is provided. Duplicate notifications for the same recipient and event on the same day
     * are skipped.
     * </p>
     *
     * @param supplier a generic {@link Supplier} that provides a custom result, useful for callbacks or additional
     *                 processing after notifications are sent
     * @param birthday the {@link Birthday} object containing recipient details, including name, email, contact number,
     *                 device token, event type (e.g., "Birthday", "Anniversary", "Festival"), and optionally event name
     *                 (e.g., "Diwali" for festivals)
     * @param <T>      the type of result returned by the supplier
     * @return the result provided by the supplier after processing notifications
     *
     * @throws RuntimeException if a notification channel fails critically (e.g., due to an unhandled IOException); individual
     *                          channel failures are logged but do not halt the process
     *
     * @implNote Ensure the data source (e.g., Excel file) provides accurate and complete recipient details:
     *           <ul>
     *             <li><b>Event Type</b>: Required (e.g., "Birthday", "Anniversary", "Festival")</li>
     *             <li><b>Event Name</b>: Optional, required only for festivals (e.g., "Diwali", "Holi")</li>
     *             <li><b>Contact Details</b>: At least one of email, contact number, or device token must be present
     *                 for notifications to be sent</li>
     *           </ul>
     *           Missing or invalid contact details result in skipped notifications for that channel, logged as warnings.
     *
     * @see java.util.function.Supplier
     * @see com.cwc.birthday.notification.model.Birthday
     * @see com.cwc.birthday.notification.utils.message.MessagePicker
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

        // E.164 format (+) for phone number
        String phoneNumber = PhoneNumberUtil.formatToE164(contactNumber);

        String subject = "🎉 " + birthday.getMessageType() + " Wishes for " + birthday.getName();
        String htmlMessageFormat = "<h2>Dear " + birthday.getName() + ",</h2><p>" + message + "</p>";

        // TODO: Email Notification
        Optional.ofNullable(email)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        e -> {
                            try {
                                emailNotification.sendEmail(e, subject, htmlMessageFormat, true);
                                log.info("✅ Email sent successfully to {} for {}", e, eventType);
                            } catch (Exception ex) {
                                log.error("❌ Failed to send email to {}: {}", e, ex.getMessage());
                            }
                        },
                        () -> log.warn("⚠️ No email sent: Email address missing for {}", name)
                );

        //TODO: SMS Notification
        Optional.of(phoneNumber)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        number -> {
                            try {
                                smsNotification.sendSms(number, message);
                                log.info("✅ SMS sent successfully to {} for {}", number, eventType);
                            } catch (IOException ex) {
                                log.error("❌ Failed to send SMS to {}: {}", number, ex.getMessage());
                            }
                        },
                        () -> log.warn("⚠️ No SMS sent: Contact number missing for {}", name)
                );

        //TODO: Push Notification
        Optional.ofNullable(deviceToken)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        token -> {
                            try {
                                pushNotification.sendPushNotification(token, subject, message);
                                log.info("✅ Push notification sent successfully to {} for {}", name, eventType);
                            } catch (IOException ex) {
                                log.error("❌ Failed to send push notification to {}: {}", name, ex.getMessage());
                            }
                        },
                        () -> log.warn("⚠️ No push notification sent: Device token missing for {}", name)
                );

        //TODO: WhatsApp Notification
        Optional.of(phoneNumber)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        number -> {
                            try {
                                whatsAppNotification.sendWhatsAppMessage(number, message);
                                log.info("✅ WhatsApp message sent successfully to {} for {}", number, eventType);
                            } catch (IOException ex) {
                                log.error("❌ Failed to send WhatsApp message to {}: {}", number, ex.getMessage());
                            }
                        },
                        () -> log.warn("⚠️ No WhatsApp message sent: Contact number missing for {}", name)
                );

        //TODO: Voice Call Notification
        Optional.of(phoneNumber)
                .filter(StringUtils::hasText)
                .ifPresentOrElse(
                        number -> {
                            try {
                                voiceCallNotification.makePhoneCall(number);
                                log.info("✅ Voice call initiated successfully to {} for {}", number, eventType);
                            } catch (IOException ex) {
                                log.error("❌ Failed to initiate voice call to {}: {}", number, ex.getMessage());
                            }
                        },
                        () -> log.warn("⚠️ No voice call initiated: Contact number missing for {}", name)
                );

        // Log the successful notification
        NotificationLog logEntry = new NotificationLog(email, phoneNumber, eventType, eventName, today);
        logRepository.save(logEntry);
        log.info("📝 Notification logged for {}: {} ({})", name, eventType, eventName);

        return supplier.get();
    }

}
