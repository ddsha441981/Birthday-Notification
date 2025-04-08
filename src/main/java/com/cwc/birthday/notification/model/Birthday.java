package com.cwc.birthday.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "birthdays")
public class Birthday implements Serializable {
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
    private String messageType;
    private String eventName;


    public Birthday(Long s_no, String name, LocalDate birthDate, String email, String contactNumber, String deviceToken, String messageType, String eventName) {
        this.s_no = s_no;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.contactNumber = contactNumber;
        this.deviceToken = deviceToken;
        this.messageType = messageType;
        this.eventName = eventName;
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
