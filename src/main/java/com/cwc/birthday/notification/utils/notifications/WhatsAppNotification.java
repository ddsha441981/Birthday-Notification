package com.cwc.birthday.notification.utils.notifications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
public class WhatsAppNotification {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromWhatsAppNumber;

    public void sendWhatsAppMessage(String phoneNumber, String message) throws IOException {
        Twilio.init(accountSid, authToken);

        try {
            Message
                    .creator(
                            new PhoneNumber("whatsapp:" + phoneNumber),
                            new PhoneNumber("whatsapp:" + fromWhatsAppNumber),
                            message
                    )
                    .create();

        } catch (ApiException e) {
            throw new IOException("Failed to send WhatsApp message: " + e.getMessage(), e);
        }
    }
}