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

    // TODO: Implementation make call logic
    public void makePhoneCall(String toPhoneNumber) throws IOException {
        Twilio.init(accountSid, authToken);

        try {
            Call.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(fromPhoneNumber),
                            //TODO: Need to implement to read custom message from .txt file  then create own twinUrl and pass dynamically your message
                            URI.create(twimlUrl)//Currently reading default message when call initiated
                    )
                    .create();
        } catch (ApiException e) {
            throw new IOException("Failed to initiate call: " + e.getMessage(), e);
        }
    }
}
