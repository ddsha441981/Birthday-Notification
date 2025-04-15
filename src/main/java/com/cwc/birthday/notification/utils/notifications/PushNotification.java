package com.cwc.birthday.notification.utils.notifications;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class PushNotification {

    private static final Logger logger = LoggerFactory.getLogger(PushNotification.class);

    @Value("${onesignal.app-id}")
    private String appId;

    @Value("${onesignal.api-key}")
    private String apiKey;

    private static final String ONESIGNAL_URL = "https://onesignal.com/api/v1/notifications";

    private final OkHttpClient client;

    public PushNotification(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Sends a push notification to a specific device using OneSignal.
     *
     * @param deviceToken     the unique FCM/OneSignal device token (player ID) to which the notification should be sent
     * @param title           the title of the push notification
     * @param messageContent  the main body content of the push notification
     * @throws IOException if an I/O error occurs during the HTTP request or if the response is unsuccessful
     */
    // TODO: Implementation push notification logic
    public void sendPushNotification(String deviceToken, String title, String messageContent) throws IOException {
        // TODO: No subscribers are used currently.
        // If needed in the future, you can retrieve subscribed users from your database

        //TODO: Not using device token but we are here using for future implementation of FCM
        deviceToken = StringUtils.isEmpty(deviceToken) ? "" : deviceToken;

        String jsonBody = String.format(
                "{\"app_id\": \"%s\", " +
                        "\"included_segments\": [\"Subscribed Users\"], " +
                        "\"headings\": {\"en\": \"%s\"}, " +
                        "\"contents\": {\"en\": \"%s\"}}",
                appId, title, messageContent
        );

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(ONESIGNAL_URL)
                .post(body)
                .addHeader("Authorization", "Basic " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "No Response";
            if (!response.isSuccessful()) {
                logger.error("Failed to send notification: {}", responseBody);
                throw new IOException("Failed to send notification: " + responseBody);
            }
            logger.info("Notification sent successfully: {}", responseBody);
        }
    }
}
