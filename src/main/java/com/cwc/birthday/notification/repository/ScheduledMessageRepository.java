package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.MessageStatus;
import com.cwc.birthday.notification.model.ScheduledMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface ScheduledMessageRepository extends JpaRepository<ScheduledMessage, Long> {// QuerydslPredicateExecutor<ScheduledMessage>
    List<ScheduledMessage> findByScheduledAtBeforeAndStatus(LocalDateTime time, MessageStatus status);

    Optional<ScheduledMessage> findBySchedulerId(String schedulerId);

    @Modifying
    @Query("DELETE FROM ScheduledMessage s WHERE s.schedulerId = :schedulerId")
    void deleteBySchedulerId(@Param("schedulerId") String schedulerId);
}
