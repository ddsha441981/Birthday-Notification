package com.cwc.birthday.notification.repository;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface BirthdayRepository extends JpaRepository<Birthday, Long>{//QuerydslPredicateExecutor<Birthday>

    @Query("SELECT b FROM Birthday b WHERE FUNCTION('MONTH', b.birthDate) = :month AND FUNCTION('DAY', b.birthDate) = :day")
    List<Birthday> findByMonthAndDay(@Param("month") int month, @Param("day") int day);


    @Query("SELECT b FROM Birthday b WHERE b.name = :name AND b.birthDate = :birthDate AND b.email = :email")
    List<Birthday> findAllExisting(@Param("name") String name, @Param("birthDate") LocalDate birthDate, @Param("email") String email);


    @Query("SELECT b FROM Birthday b WHERE FUNCTION('MONTH', b.birthDate) = :month AND FUNCTION('DAY', b.birthDate) = :day")
    Page<Birthday> findByMonthAndDay(@Param("month") int month, @Param("day") int day,Pageable pageable);

}
