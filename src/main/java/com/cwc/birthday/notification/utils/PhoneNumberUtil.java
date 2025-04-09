package com.cwc.birthday.notification.utils;

import org.springframework.stereotype.Component;

@Component
public class PhoneNumberUtil {

    /**
     * Converts a phone number to E.164 format by adding +91 if missing.
     * Trims whitespace and validates the final format.
     *
     * @param phoneNumber the original phone number
     * @return formatted phone number in E.164
     * @throws IllegalArgumentException if the phone number is invalid
     */
    public static String formatToE164(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty.");
        }

        phoneNumber = phoneNumber.trim().replaceAll("\\s+", "");

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+91" + phoneNumber;
        }
        if (!phoneNumber.matches("^\\+\\d{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }

        return phoneNumber;
    }
}

