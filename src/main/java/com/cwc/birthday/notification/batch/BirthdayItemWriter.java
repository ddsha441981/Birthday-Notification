package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

public class BirthdayItemWriter implements ItemWriter<Birthday> {
//    @Override
//    public void write(List<? extends Birthday> birthdays) throws Exception {
//        // No-op if service handles persistence
//    }

    @Override
    public void write(Chunk<? extends Birthday> chunk) throws Exception {
        // No-op if service handles persistence
    }
}
