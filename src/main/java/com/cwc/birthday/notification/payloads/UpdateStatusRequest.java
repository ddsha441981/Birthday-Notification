package com.cwc.birthday.notification.payloads;

import java.time.LocalDateTime;

public class UpdateStatusRequest {
    private String status;
    private LocalDateTime scheduledAt;
}
