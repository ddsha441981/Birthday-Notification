package com.cwc.birthday.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "birthdays")
public class Birthday {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Transient
    private Long s_no;
    private String name;
    private LocalDate birthDate;
    private String email;
    private String contactNumber;
    private String deviceToken;


    public Birthday(Long sno, String name, LocalDate birthDate, String email, String contactNumber, String deviceToken) {
        this.s_no = sno;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.contactNumber = contactNumber;
        this.deviceToken = deviceToken;
    }

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
