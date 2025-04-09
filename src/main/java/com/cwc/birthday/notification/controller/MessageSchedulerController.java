package com.cwc.birthday.notification.controller;

import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.payloads.MessageScheduleRequest;
import com.cwc.birthday.notification.service.MessageSchedulerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scheduler")
public class MessageSchedulerController {

    private final MessageSchedulerService schedulerService;

    public MessageSchedulerController(MessageSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduledMessage> scheduleMessage(@RequestBody MessageScheduleRequest request) {
        ScheduledMessage scheduledMessage = schedulerService.scheduleMessage(request);
        return ResponseEntity.ok(scheduledMessage);
    }

    @GetMapping
    public ResponseEntity<Page<ScheduledMessage>> getAllScheduledMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ScheduledMessage> scheduledMessages = schedulerService.findScheduledMessages(page, size);
        return new ResponseEntity<>(scheduledMessages, HttpStatus.OK);
    }
}
