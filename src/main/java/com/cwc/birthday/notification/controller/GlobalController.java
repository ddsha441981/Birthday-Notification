package com.cwc.birthday.notification.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GlobalController {

    @Value("${app.base-url}")
    private String baseUrl;

    //Check endpoints
    @GetMapping
//    @ResponseBody
    public String accessEndPoint(Model model) {
        model.addAttribute("baseUrl", baseUrl);
        return "home";
    }

    //For Schedule Message Endpoint
    @RequestMapping("/scheduler")
    public String schedulerPage() {
        return "message-scheduler";
    }
}
