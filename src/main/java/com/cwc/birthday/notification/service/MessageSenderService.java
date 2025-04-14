package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.ScheduledMessage;

@FunctionalInterface
public interface MessageSenderService {
    void sendMessage(ScheduledMessage message);
}
