package com.cwc.birthday.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "birthdays")
public class Birthday {
    @Id
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String email;
    private String contactNumber;
    private String deviceToken;

    /**
     * Avoids potential ClassCastException if subclass
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Birthday)) return false;
        Birthday that = (Birthday) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(deviceToken, that.deviceToken) &&
                Objects.equals(birthDate, that.birthDate);
    }

    /**
     * To prevents NullPointerException
     * Adds uniqueness to each person (can omit if name+birthDate is enough)
     * */
    @Override
    public int hashCode() {
        return Objects.hash(name, email, deviceToken, birthDate);
    }
}
