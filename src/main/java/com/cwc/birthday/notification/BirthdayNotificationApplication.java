package com.cwc.birthday.notification;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class BirthdayNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirthdayNotificationApplication.class, args);
	}

}
