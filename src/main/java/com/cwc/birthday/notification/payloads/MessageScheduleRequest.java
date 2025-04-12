package com.cwc.birthday.notification.payloads;

import com.cwc.birthday.notification.model.ChannelTypes;
import com.cwc.birthday.notification.model.MessageStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageScheduleRequest {
    private String schedulerId;
    private String message;
    private List<String> phoneNumbers;
    private List<String> emailAddresses;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private Set<ChannelTypes> channels;
}
