package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BirthdayService {

     boolean isDuplicate(Birthday birthday);
     boolean exists(Birthday birthday);

     Birthday findById(Long id);

     void addBirthday(Birthday birthday);

     List<Birthday> getBirthdayList();
     Page<Birthday> getBirthdayList(int page, int size);

     void updateBirthday(Birthday birthday,Long id);

     List<Birthday> getTodayBirthdays();
}
