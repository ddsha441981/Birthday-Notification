package com.cwc.birthday.notification.service;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface BirthdayService {

    public Birthday findById(Long id);
    public void addBirthday(Birthday birthday);

    public void updateBirthday(Birthday birthday);
}
