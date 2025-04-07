package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.repository.BirthdayRepository;
import com.cwc.birthday.notification.service.BirthdayService;
import org.springframework.stereotype.Service;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private final  BirthdayRepository birthdayRepository;

    public BirthdayServiceImpl(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    @Override
    public Birthday findById(Long id) {
       // birthdayRepository.findById(id).orElse(null);
        return null;
    }

    @Override
    public void addBirthday(Birthday birthday) {
        //birthdayRepository.save(birthday);
    }

    @Override
    public void updateBirthday(Birthday birthday) {
        //birthdayRepository.save(birthday);
    }
}
