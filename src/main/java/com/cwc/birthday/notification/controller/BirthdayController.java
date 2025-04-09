package com.cwc.birthday.notification.controller;

import com.cwc.birthday.notification.exceptions.ApiResponse;
import com.cwc.birthday.notification.exceptions.ResourceNotFoundException;
import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/birthday")
@RestController
@Slf4j
public class BirthdayController {

    private final BirthdayService birthdayService;

    public BirthdayController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }



    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<Birthday>>> getAllBirthdays(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Birthday> birthdayList = birthdayService.getBirthdayList(page, size);

        ApiResponse<Page<Birthday>> response = new ApiResponse<>(
                birthdayList,
                "Resource fetched successfully",
                true,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


}
