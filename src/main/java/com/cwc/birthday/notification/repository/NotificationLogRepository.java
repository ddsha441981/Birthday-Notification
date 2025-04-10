package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {//QuerydslPredicateExecutor<NotificationLog>
    boolean existsByEmailAndEventTypeAndEventNameAndSentDate(
            String email, String eventType, String eventName, LocalDate sentDate);
}

