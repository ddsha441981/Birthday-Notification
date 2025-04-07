package com.cwc.birthday.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Birthday {
    @Id
    private Long id;
    private String name;
    private LocalDate birthDate;

    // Getters, setters, and constructors
    public Birthday() {}

    public Birthday(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Birthday birthday = (Birthday) o;
        return name.equals(birthday.name) && birthDate.equals(birthday.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate);
    }
}
