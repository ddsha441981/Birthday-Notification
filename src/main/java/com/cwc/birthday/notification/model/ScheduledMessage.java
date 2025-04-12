package com.cwc.birthday.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_message")
public class ScheduledMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true, length = 36)
    private String schedulerId;

    @PrePersist
    public void prePersist() {
        if (schedulerId == null) {
            schedulerId = UUID.randomUUID().toString();
        }
    }

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
