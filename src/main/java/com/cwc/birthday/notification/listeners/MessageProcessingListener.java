package com.cwc.birthday.notification.listeners;

import com.cwc.birthday.notification.model.MessageStatus;
import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.repository.ScheduledMessageRepository;
import com.cwc.birthday.notification.service.MessageSenderService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageProcessingListener {

    private final MessageSenderService messageSenderService;
    private final ScheduledMessageRepository messageRepository;

    public MessageProcessingListener(MessageSenderService messageSenderService,
                                     ScheduledMessageRepository messageRepository) {
        this.messageSenderService = messageSenderService;
        this.messageRepository = messageRepository;
    }

    @Async
    @EventListener
    public void handleScheduledMessage(ScheduledMessage message) {
        try {
            message.setStatus(MessageStatus.IN_PROGRESS);
            messageRepository.save(message);

            messageSenderService.sendMessage(message);

            message.setStatus(MessageStatus.SENT);
            message.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            message.setStatus(MessageStatus.FAILED);
        } finally {
            messageRepository.save(message);
        }
    }
}
