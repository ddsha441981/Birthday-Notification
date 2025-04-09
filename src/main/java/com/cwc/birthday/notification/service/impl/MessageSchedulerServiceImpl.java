
package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.ChannelTypes;
import com.cwc.birthday.notification.model.MessageStatus;
import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.payloads.MessageScheduleRequest;
import com.cwc.birthday.notification.repository.ScheduledMessageRepository;
import com.cwc.birthday.notification.service.MessageSchedulerService;
import com.cwc.birthday.notification.service.MessageSenderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class MessageSchedulerServiceImpl implements MessageSchedulerService {

    private final ScheduledMessageRepository messageRepository;
    private final MessageSenderService messageSenderService;
    private final ApplicationEventPublisher eventPublisher;

    public MessageSchedulerServiceImpl(ScheduledMessageRepository messageRepository,
                                       MessageSenderService messageSenderService,
                                       ApplicationEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.messageSenderService = messageSenderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ScheduledMessage scheduleMessage(MessageScheduleRequest request) {
        ScheduledMessage scheduledMessage = scheduleMessageBuilder(request);
        validateMessageRequest(scheduledMessage);
        ScheduledMessage saved = messageRepository.save(scheduledMessage);
        return saved;
    }

    private ScheduledMessage scheduleMessageBuilder(MessageScheduleRequest request) {
        return ScheduledMessage.builder()
                .message(request.getMessage())
                .phoneNumbers(new ArrayList<>(request.getPhoneNumbers()))
                .emailAddresses(new ArrayList<>(request.getEmailAddresses()))
                .scheduledAt(request.getScheduledAt())
                .channels(new HashSet<>(request.getChannels()))
                .status(MessageStatus.SCHEDULED)
                .build();
    }

    private void validateMessageRequest(ScheduledMessage message) {
        boolean needsPhoneNumbers = message.getChannels().contains(ChannelTypes.SMS) ||
                message.getChannels().contains(ChannelTypes.WHATSAPP) ||
                message.getChannels().contains(ChannelTypes.VOICE) ||
                message.getChannels().contains(ChannelTypes.PUSH);

        if (needsPhoneNumbers && (message.getPhoneNumbers() == null || message.getPhoneNumbers().isEmpty())) {
            throw new IllegalArgumentException("Phone numbers are required for the selected channels");
        }

        if (message.getChannels().contains(ChannelTypes.EMAIL) &&
                (message.getEmailAddresses() == null || message.getEmailAddresses().isEmpty())) {
            throw new IllegalArgumentException("Email addresses are required for the Email channel");
        }
    }

    @Override
    public List<ScheduledMessage> findAllScheduledMessages() {
        return this.messageRepository.findAll();
    }

    @Override
    public Page<ScheduledMessage> findScheduledMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledAt").descending());
        return messageRepository.findAll(pageable);
    }


    @Override
    public void processScheduledMessages() {
        LocalDateTime now = LocalDateTime.now();
        List<ScheduledMessage> messagesToSend = messageRepository.findByScheduledAtBeforeAndStatus(
                now, MessageStatus.SCHEDULED);

        for (ScheduledMessage message : messagesToSend) {
            // Publish event asynchronously
            eventPublisher.publishEvent(message);
        }
    }
}














//package com.cwc.birthday.notification.service.impl;
//
//import com.cwc.birthday.notification.model.ChannelTypes;
//import com.cwc.birthday.notification.model.MessageStatus;
//import com.cwc.birthday.notification.model.ScheduledMessage;
//import com.cwc.birthday.notification.payloads.MessageScheduleRequest;
//import com.cwc.birthday.notification.repository.ScheduledMessageRepository;
//import com.cwc.birthday.notification.service.MessageSchedulerService;
//import com.cwc.birthday.notification.service.MessageSenderService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//@Transactional
//@Service
//public class MessageSchedulerServiceImpl implements MessageSchedulerService {
//
//
//    private final ScheduledMessageRepository messageRepository;
//    private final MessageSenderService messageSenderService;
//
//    public MessageSchedulerServiceImpl(ScheduledMessageRepository messageRepository, MessageSenderService messageSenderService) {
//        this.messageRepository = messageRepository;
//        this.messageSenderService = messageSenderService;
//    }
//
//    @Override
//    public ScheduledMessage scheduleMessage(MessageScheduleRequest request) {
//
//        ScheduledMessage scheduledMessage = scheduleMessageBuilder(request);
//        validateMessageRequest(scheduledMessage);
//        return messageRepository.save(scheduledMessage);
//    }
//
//    private ScheduledMessage scheduleMessageBuilder(MessageScheduleRequest request) {
//        ScheduledMessage message = ScheduledMessage.builder()
//                .message(request.getMessage())
//                .phoneNumbers(new ArrayList<>(request.getPhoneNumbers()))
//                .emailAddresses(new ArrayList<>(request.getEmailAddresses()))
//                .scheduledAt(request.getScheduledAt())
//                .channels(new HashSet<>(request.getChannels()))
//                .status(MessageStatus.SCHEDULED)
//                .build();
//        return message;
//    }
//
//    private void validateMessageRequest(ScheduledMessage message) {
//
//        boolean needsPhoneNumbers = message.getChannels().contains(ChannelTypes.SMS) ||
//                message.getChannels().contains(ChannelTypes.WHATSAPP) ||
//                message.getChannels().contains(ChannelTypes.VOICE) ||
//                message.getChannels().contains(ChannelTypes.PUSH);
//
//        if (needsPhoneNumbers && (message.getPhoneNumbers() == null || message.getPhoneNumbers().isEmpty())) {
//            throw new IllegalArgumentException("Phone numbers are required for the selected channels");
//        }
//
//        if (message.getChannels().contains(ChannelTypes.EMAIL) &&
//                (message.getEmailAddresses() == null || message.getEmailAddresses().isEmpty())) {
//            throw new IllegalArgumentException("Email addresses are required for the Email channel");
//        }
//    }
//
//
//    @Override
//    public List<ScheduledMessage> findAllScheduledMessages() {
//        return this.messageRepository.findAll();
//    }
//
//    @Override
//    public void processScheduledMessages() {
//
//        LocalDateTime now = LocalDateTime.now();
//        List<ScheduledMessage> messagesToSend = messageRepository.findByScheduledAtBeforeAndStatus(
//                now, MessageStatus.SCHEDULED);
//
//        for (ScheduledMessage message : messagesToSend) {
//            try {
//                message.setStatus(MessageStatus.IN_PROGRESS);
//                messageRepository.save(message);
//
//                messageSenderService.sendMessage(message);
//
//                message.setStatus(MessageStatus.SENT);
//                message.setSentAt(LocalDateTime.now());
//            } catch (Exception e) {
//                message.setStatus(MessageStatus.FAILED);
//            } finally {
//                messageRepository.save(message);
//            }
//        }
//    }
//}
