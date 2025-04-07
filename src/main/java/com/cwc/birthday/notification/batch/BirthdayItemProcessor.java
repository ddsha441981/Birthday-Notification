package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.batch.item.ItemProcessor;

public class BirthdayItemProcessor implements ItemProcessor<Birthday, Birthday> {

    @Override
    public Birthday process(Birthday item) {
        return item;
    }
}
