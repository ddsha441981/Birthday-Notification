package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import com.cwc.birthday.notification.service.BirthdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.Optional;

public class BirthdayItemWriter implements ItemWriter<Birthday> {
    private final Logger log = LoggerFactory.getLogger(BirthdayItemWriter.class);

    private final BirthdayService birthdayService;

    public BirthdayItemWriter(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

    @Override
    public void write(Chunk<? extends Birthday> chunk) throws Exception {
        for (Birthday birthday : chunk) {
            if (birthday == null) {
                log.warn("❌ Skipped null birthday record.");
                continue;
            }

            log.info("🔄 Processing birthday: {}", birthday);

            //exist → duplicate → skip
            if (birthdayService.isDuplicate(birthday)) {
                log.warn("⚠️ Duplicate record found for {} → Skipping insert", birthday.getEmail());
                continue;
            }

            // ✅  → insert
            if (!birthdayService.exists(birthday)) {
                log.info("🆕 No existing record found → Inserting");
                birthdayService.addBirthday(birthday);
            } else {
                log.info("✅ Already exists → Skipping");
            }
        }
    }



//    @Override
//    public void write(Chunk<? extends Birthday> chunk) throws Exception {
//        for (Birthday birthday : chunk) {
//            if (birthday == null) {
//                log.warn("Skipped null birthday record.");
//                continue;
//            }
//            Long id = birthday.getId();
//            log.info("🔄 Processing birthday: " ,birthday);
//            if (id == null) {
//                log.info("🆕 ID is null → Adding new birthday");
//                birthdayService.addBirthday(birthday);
//                continue;
//            }
//            Birthday existing = birthdayService.findById(id);
//
//            if (existing == null) {
//                log.info("➕ No existing record found → Inserting");
//                birthdayService.addBirthday(birthday);
//            } else if (!existing.equals(birthday)) {
//                log.info("✏️ Record changed → Updating");
//                birthdayService.updateBirthday(birthday,id);
//            } else {
//                log.info("✅ Record unchanged → Skipping");
//            }
//        }
//    }
}
