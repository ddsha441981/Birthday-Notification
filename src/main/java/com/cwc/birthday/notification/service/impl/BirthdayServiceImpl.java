package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.repository.BirthdayRepository;
import com.cwc.birthday.notification.service.BirthdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdayServiceImpl implements BirthdayService {
    private static final Logger log = LoggerFactory.getLogger(BirthdayServiceImpl.class);
    private final BirthdayRepository birthdayRepository;

    public BirthdayServiceImpl(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    @Override
    public Birthday findById(Long id) {
        return birthdayRepository.findById(id).orElse(null);
    }

    @Override
    public boolean isDuplicate(Birthday birthday) {
        List<Birthday> existing = birthdayRepository.findAllExisting(
                birthday.getName(),
                birthday.getBirthDate(),
                birthday.getEmail()
        );
        return existing.size() > 1;
    }

    @Override
    public boolean exists(Birthday birthday) {
        List<Birthday> existing = birthdayRepository.findAllExisting(
                birthday.getName(),
                birthday.getBirthDate(),
                birthday.getEmail()
        );
        return !existing.isEmpty();
    }


    @Override
    public void addBirthday(Birthday birthday) {
        log.info("✅ Saving record for: {}", birthday.getName());
        birthdayRepository.save(birthday);
    }

    @Override
    public Page<Birthday> getBirthdayList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return birthdayRepository.findAll(pageable);
    }


    @Override
    public List<Birthday> getBirthdayList() {
        return this.birthdayRepository.findAll();
    }

    @Override
    public void updateBirthday(Birthday birthday, Long id) {
        Birthday currentBirthdayData = this.birthdayRepository.findById(id).orElse(null);
        if (currentBirthdayData != null) {
            birthday.setId(id);
            birthday.setName(currentBirthdayData.getName());
            birthday.setBirthDate(currentBirthdayData.getBirthDate());
            birthday.setEmail(currentBirthdayData.getEmail());
            birthday.setContactNumber(currentBirthdayData.getContactNumber());
            birthday.setDeviceToken(currentBirthdayData.getDeviceToken());
            birthdayRepository.save(birthday);
        }
        birthdayRepository.save(birthday);
    }

    @Override
    public List<Birthday> getTodayBirthdays() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        return birthdayRepository.findByMonthAndDay(month, day);
    }
}
