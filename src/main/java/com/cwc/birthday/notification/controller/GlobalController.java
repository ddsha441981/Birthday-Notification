package com.cwc.birthday.notification.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalController {
    @GetMapping
    public String accessEndPoint() {
        return "Welcome to the Birthday Notification App!";
    }
}
