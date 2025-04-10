package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.MessageStatus;
import com.cwc.birthday.notification.model.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {// QuerydslPredicateExecutor<ScheduledMessage>
    List<ScheduledMessage> findByScheduledAtBeforeAndStatus(LocalDateTime time, MessageStatus status);
}
