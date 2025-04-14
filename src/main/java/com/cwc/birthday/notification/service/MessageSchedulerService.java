package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.payloads.MessageScheduleRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageSchedulerService {
    ScheduledMessage scheduleMessage(MessageScheduleRequest request);

    List<ScheduledMessage> findAllScheduledMessages();

    Page<ScheduledMessage> findScheduledMessages(int page, int size);

    void processScheduledMessages();

    ScheduledMessage updateScheduleMessage(MessageScheduleRequest request, String schedulerId);

    ScheduledMessage findBySchedulerIdMessage(String schedulerId);

    void deleteScheduleMessage(String schedulerId);

    boolean changeStatusWhenScheduleMessageCancelled(String schedulerId);
}
