package com.cwc.birthday.notification.utils.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class MessagePicker {
    private final Logger log = LoggerFactory.getLogger(MessagePicker.class);

    private static final String BASE_PATH = "messages/";

    public String pickRandomMessage(String messageType, String eventName) {
        try {
            String fileName;
            if ("Festival".equalsIgnoreCase(messageType) && eventName != null) {
                fileName = "festival-" + eventName.toLowerCase() + ".txt";
            } else {
                fileName = messageType.toLowerCase() + ".txt";
            }

            ClassPathResource resource = new ClassPathResource(BASE_PATH + fileName);
            List<String> lines = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines()
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList());

            if (lines.isEmpty()) throw new RuntimeException("No messages in file: " + fileName);

            return lines.get(new Random().nextInt(lines.size()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading message file", e);
        }
    }
}

