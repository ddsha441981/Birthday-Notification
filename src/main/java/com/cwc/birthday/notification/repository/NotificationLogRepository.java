package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RepositoryRestResource(exported = false)
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {//QuerydslPredicateExecutor<NotificationLog>
    boolean existsByEmailAndEventTypeAndEventNameAndSentDate(
            String email, String eventType, String eventName, LocalDate sentDate);
}

