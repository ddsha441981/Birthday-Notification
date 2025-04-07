package com.cwc.birthday.notification.batch;
import com.cwc.birthday.notification.model.Birthday;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ExcelItemReader implements ItemReader<Birthday> {

    private Iterator<Row> rowIterator;

    public ExcelItemReader() throws Exception {
        ClassPathResource resource = new ClassPathResource("birthdays.xlsx");
        try (InputStream is = resource.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // First sheet
            rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row
        }
    }

    @Override
    public Birthday read() throws Exception {
        if (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Long id = (long) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String dateStr = row.getCell(2).getStringCellValue();
            LocalDate birthDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return new Birthday(id, name, birthDate);
        }
        return null; // Null indicates end of data
    }
}
