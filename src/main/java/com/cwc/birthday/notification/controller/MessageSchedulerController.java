package com.cwc.birthday.notification.controller;

import com.cwc.birthday.notification.model.ScheduledMessage;
import com.cwc.birthday.notification.payloads.MessageScheduleRequest;
import com.cwc.birthday.notification.service.MessageSchedulerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        //To print Scheduled Messages ID
        scheduledMessages.stream().forEach(scheduledMessage -> {
            System.out.println(scheduledMessage.getSchedulerId());
        });
        return new ResponseEntity<>(scheduledMessages, HttpStatus.OK);
    }

    @GetMapping("/{schedulerId}")
    public ResponseEntity<ScheduledMessage> getScheduledMessageBySchedulerId(@PathVariable("schedulerId") String schedulerId) {
        ScheduledMessage scheduledMessage = this.schedulerService.findBySchedulerIdMessage(schedulerId);
        return new ResponseEntity<>(scheduledMessage, HttpStatus.OK);
    }

    @PutMapping("/update/{schedulerId}")
    public ResponseEntity<ScheduledMessage> updateScheduledMessage(
            @PathVariable("schedulerId") String schedulerId,
            @RequestBody MessageScheduleRequest request) {
        ScheduledMessage updatedMessage = schedulerService.updateScheduleMessage(request, schedulerId);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/delete/{schedulerId}")
    public ResponseEntity<Void> deleteScheduledMessage(@PathVariable String schedulerId) {
        schedulerService.deleteScheduleMessage(schedulerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/status/{schedulerId}")
    public ResponseEntity<Boolean> updateStatusWhenCancelScheduleMessage(
            @PathVariable("schedulerId") String schedulerId) {
        boolean messageCancelled = schedulerService.changeStatusWhenScheduleMessageCancelled(schedulerId);
        return ResponseEntity.ok(messageCancelled);
    }

}
