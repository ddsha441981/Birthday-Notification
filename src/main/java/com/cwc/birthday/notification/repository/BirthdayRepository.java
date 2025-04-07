package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BirthdayRepository extends JpaRepository<Birthday, Long> {

    @Query("SELECT b FROM Birthday b WHERE FUNCTION('MONTH', b.birthDate) = :month AND FUNCTION('DAY', b.birthDate) = :day")
    List<Birthday> findByMonthAndDay(@Param("month") int month, @Param("day") int day);


    @Query("SELECT b FROM Birthday b WHERE b.name = :name AND b.birthDate = :birthDate AND b.email = :email")
    List<Birthday> findAllExisting(@Param("name") String name, @Param("birthDate") LocalDate birthDate, @Param("email") String email);


}
