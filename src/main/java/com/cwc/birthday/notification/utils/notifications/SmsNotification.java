package com.cwc.birthday.notification.utils.notifications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SmsNotification {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromNumber;


    public void sendSms(String phoneNumber, String message) throws IOException {
        // TODO: Implementation sms logic

        try {
            Twilio.init(accountSid, authToken);

            if (!phoneNumber.matches("^\\+\\d{10,15}$")) {
                throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
            }
            Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber(fromNumber),
                            message)
                    .create();
        } catch (Exception e) {
            throw new IOException("Failed to send SMS message: " + e.getMessage(), e);
        }
    }

}
