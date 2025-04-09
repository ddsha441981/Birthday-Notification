package com.cwc.birthday.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GlobalController {

    //Check endpoints
    @GetMapping
    @ResponseBody
    public String accessEndPoint() {
        return "Welcome to the Birthday Notification App!";
    }

    //For Schedule Message Endpoint
    @RequestMapping("/scheduler")
    public String schedulerPage() {
        return "message-scheduler";
    }
}
