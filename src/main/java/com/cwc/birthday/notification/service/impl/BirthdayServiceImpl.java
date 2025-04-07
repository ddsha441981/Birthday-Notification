package com.cwc.birthday.notification.service.impl;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.repository.BirthdayRepository;
import com.cwc.birthday.notification.service.BirthdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//        final String DEFAULT_NAME = "Unknown";
//        final String DEFAULT_EMAIL = "unknown@example.com";
//        final String DEFAULT_CONTACT = "0000000000";
//        final String DEFAULT_DEVICE_TOKEN = "NO_TOKEN";
//
//        if (!StringUtils.hasText(birthday.getName()) || !StringUtils.hasText(birthday.getEmail()) || !StringUtils.hasText(birthday.getContactNumber()) || !StringUtils.hasText(birthday.getDeviceToken())) {
//
//            log.warn("Some fields were missing. Applying default values where necessary.");
//
//            birthday.setName(StringUtils.hasText(birthday.getName()) ? birthday.getName() : DEFAULT_NAME);
//            birthday.setEmail(StringUtils.hasText(birthday.getEmail()) ? birthday.getEmail() : DEFAULT_EMAIL);
//            birthday.setContactNumber(StringUtils.hasText(birthday.getContactNumber()) ? birthday.getContactNumber() : DEFAULT_CONTACT);
//            birthday.setDeviceToken(StringUtils.hasText(birthday.getDeviceToken()) ? birthday.getDeviceToken() : DEFAULT_DEVICE_TOKEN);
//        }

        log.info("✅ Saving birthday record for: {}", birthday.getName());
        birthdayRepository.save(birthday);
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
        List<Birthday> birthdayList = birthdayRepository.findByMonthAndDay(month, day);
        return birthdayList;
    }
}
