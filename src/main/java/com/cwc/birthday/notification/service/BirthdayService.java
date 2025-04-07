package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BirthdayService {

     Birthday findById(Long id);

     void addBirthday(Birthday birthday);

     List<Birthday> getBirthdayList();

     void updateBirthday(Birthday birthday,Long id);

     List<Birthday> getTodayBirthdays();
}
