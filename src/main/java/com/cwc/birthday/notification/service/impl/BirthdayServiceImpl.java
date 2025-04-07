package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.exceptions.ResourceNotFoundException;
import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.repository.BirthdayRepository;
import com.cwc.birthday.notification.service.BirthdayService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private final  BirthdayRepository birthdayRepository;

    public BirthdayServiceImpl(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    @Override
    public Birthday findById(Long id) {
        return birthdayRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Birthday not found"));
    }

    @Override
    public void addBirthday(Birthday birthday) {
        //birthdayRepository.save(birthday);
    }

    @Override
    public List<Birthday> getBirthdayList() {
        return this.birthdayRepository.findAll();
    }

    @Override
    public void updateBirthday(Birthday birthday) {
        //birthdayRepository.save(birthday);
    }

    @Override
    public List<Birthday> getTodayBirthdays() {
        //implement get today birthday from db
        return List.of();
    }
}
