package com.cwc.birthday.notification.utils.notifications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class VoiceCallNotification {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Value("${twilio.url}")
    private String twimlUrl;

    public void makePhoneCall(String toPhoneNumber) throws IOException {
        Twilio.init(accountSid, authToken);

        try {
            Call.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(fromPhoneNumber),
                            URI.create(twimlUrl)
                    )
                    .create();
        } catch (ApiException e) {
            throw new IOException("Failed to initiate call: " + e.getMessage(), e);
        }
    }
}
