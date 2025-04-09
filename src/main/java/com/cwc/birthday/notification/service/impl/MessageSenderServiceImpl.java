package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.ChannelTypes;
import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.service.MessageSenderService;
import com.cwc.birthday.notification.utils.PhoneNumberUtil;
import com.cwc.birthday.notification.utils.notifications.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

    private final EmailNotification emailNotification;
    private final SmsNotification smsNotification;
    private final PushNotification pushNotification;
    private final WhatsAppNotification whatsAppNotification;
    private final VoiceCallNotification voiceCallNotification;

    public MessageSenderServiceImpl(EmailNotification emailNotification,
                                    SmsNotification smsNotification,
                                    PushNotification pushNotification,
                                    WhatsAppNotification whatsAppNotification,
                                    VoiceCallNotification voiceCallNotification) {
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
        this.pushNotification = pushNotification;
        this.whatsAppNotification = whatsAppNotification;
        this.voiceCallNotification = voiceCallNotification;
    }

    @Override
    public void sendMessage(ScheduledMessage message) {
        for (ChannelTypes channel : message.getChannels()) {
            try {
                switch (channel) {
                    case SMS -> sendSMS(message);
                    case PUSH -> sendPushNotification(message);
                    case VOICE -> sendVoiceCall(message);
                    case WHATSAPP -> sendWhatsApp(message);
                    case EMAIL -> sendEmail(message);
                    default -> logger.warn("Unsupported channel type: {}", channel);
                }
            } catch (IOException e) {
                logger.error("Failed to send message via {}: {}", channel, e.getMessage(), e);
            }
        }
    }

    private void sendSMS(ScheduledMessage message) throws IOException {
        for (String phoneNumber : message.getPhoneNumbers()) {
            String formatted = PhoneNumberUtil.formatToE164(phoneNumber);
            smsNotification.sendSms(formatted, message.getMessage());
            logger.info("SMS sent to {}: {}", formatted, message.getMessage());
        }
    }

    private void sendPushNotification(ScheduledMessage message) throws IOException {
        String title = "Push Notification";
        for (String deviceId : message.getPhoneNumbers()) {
            pushNotification.sendPushNotification(deviceId, title, message.getMessage());
            logger.info("Push notification sent to device {}: {}", deviceId, message.getMessage());
        }
    }

    private void sendVoiceCall(ScheduledMessage message) throws IOException {
        for (String phoneNumber : message.getPhoneNumbers()) {
            String formatted = PhoneNumberUtil.formatToE164(phoneNumber);
            voiceCallNotification.makePhoneCall(formatted);
            logger.info("Voice call made to {} with message: {}", formatted, message.getMessage());
        }
    }

    private void sendWhatsApp(ScheduledMessage message) throws IOException {
        for (String phoneNumber : message.getPhoneNumbers()) {
            String formatted = PhoneNumberUtil.formatToE164(phoneNumber);
            whatsAppNotification.sendWhatsAppMessage(formatted, message.getMessage());
            logger.info("WhatsApp message sent to {}: {}", formatted, message.getMessage());
        }
    }

    private void sendEmail(ScheduledMessage message) {
        for (String email : message.getEmailAddresses()) {
            emailNotification.sendEmail(email, "Notification", message.getMessage(), true);
            logger.info("Email sent to {}: {}", email, message.getMessage());
        }
    }
}
