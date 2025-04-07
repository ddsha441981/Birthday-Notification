package com.cwc.birthday.notification.controller;

import com.cwc.birthday.notification.service.BirthdayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/birthday")
@RestController
@Slf4j
public class BirthdayController {

    private final BirthdayService birthdayService;

    public BirthdayController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

}
