package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BirthdayRepository extends JpaRepository<Birthday, Long> {


    @Query("SELECT b FROM Birthday b WHERE FUNCTION('MONTH', b.birthDate) = :month AND FUNCTION('DAY', b.birthDate) = :day")
    List<Birthday> findByMonthAndDay(@Param("month") int month, @Param("day") int day);


}
