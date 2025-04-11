package com.cwc.birthday.notification.controller;

import com.cwc.birthday.notification.exceptions.ApiResponse;
import com.cwc.birthday.notification.exceptions.ResourceNotFoundException;
import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    //Load Excel file from local drive
    @GetMapping("/load-excel")
    public ResponseEntity<?> loadExcel() {
        try {
            Path filePath = Paths.get("C:/data/birthdays.xlsx");
            System.out.println("Trying to read Excel file at: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + filePath.toAbsolutePath());
            }

            byte[] bytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);

        } catch (IOException e) {
            e.printStackTrace(); // See server logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading Excel file: " + e.getMessage());
        }
    }

    //After edit save Excel file
    @PostMapping("/save-excel")
    public ResponseEntity<String> saveExcel(@RequestBody byte[] fileContent) {
        try {
            Path path = Paths.get("C:/data/birthdays.xlsx");
            Files.write(path, fileContent);
            return ResponseEntity.ok("File saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving Excel file: " + e.getMessage());
        }
    }





}
