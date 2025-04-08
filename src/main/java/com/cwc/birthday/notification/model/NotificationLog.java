package com.cwc.birthday.notification.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "notification_log")
public class NotificationLog  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String contactNumber;
    private String eventType;
    private String eventName;
    private LocalDate sentDate;

    // Constructors
    public NotificationLog() {}
    public NotificationLog(String email, String contactNumber, String eventType, String eventName, LocalDate sentDate) {
        this.email = email;
        this.contactNumber = contactNumber;
        this.eventType = eventType;
        this.eventName = eventName;
        this.sentDate = sentDate;
    }
}

