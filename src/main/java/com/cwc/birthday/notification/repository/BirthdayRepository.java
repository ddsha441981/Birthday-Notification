package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthdayRepository extends JpaRepository<Birthday, Long> {
}
