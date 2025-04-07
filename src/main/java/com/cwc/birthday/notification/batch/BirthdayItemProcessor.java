package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class BirthdayItemProcessor implements ItemProcessor<Birthday, Birthday> {

    @Autowired
    private BirthdayService birthdayService;

    @Override
    public Birthday process(Birthday birthday) throws Exception {
        Long birthdayId = birthday.getId();
        Birthday existingBirthday = birthdayService.findById(birthdayId);
        if (existingBirthday == null) {
            // New birthday, call add method
            birthdayService.addBirthday(birthday);
        } else if (!existingBirthday.equals(birthday)) {
            // Existing birthday with changes, call update method
            birthdayService.updateBirthday(birthday);
        }
        return birthday; // Pass to writer if needed
    }
}
