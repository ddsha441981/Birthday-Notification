package com.cwc.birthday.notification.utils.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class BirthdayMessagePicker {
    private final Logger log = LoggerFactory.getLogger(BirthdayMessagePicker.class);

    @Value("${birthday.file.path}")
    private String  FILE_NAME;

    private final Random random = new Random();

    public String getRandomBirthdayMessage() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(FILE_NAME)))) {

            List<String> messages = reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toList());
            log.info(messages.get(random.nextInt(messages.size())));
            return messages.get(random.nextInt(messages.size()));

        } catch (Exception e) {
            e.printStackTrace();
            return "Wishing you a Happy Birthday!";
        }
    }
}

