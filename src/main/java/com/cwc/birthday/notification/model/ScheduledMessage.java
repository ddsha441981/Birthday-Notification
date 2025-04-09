package com.cwc.birthday.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_message")
public class ScheduledMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ElementCollection
    private List<String> phoneNumbers;

    @ElementCollection
    private List<String> emailAddresses;

    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ChannelTypes> channels;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SCHEDULED;
}
