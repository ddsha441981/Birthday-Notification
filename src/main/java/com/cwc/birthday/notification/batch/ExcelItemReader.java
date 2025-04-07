package com.cwc.birthday.notification.batch;

import com.cwc.birthday.notification.model.Birthday;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ExcelItemReader implements ItemReader<Birthday> {

    private Iterator<Row> rowIterator;

    public ExcelItemReader() throws Exception {
        ClassPathResource resource = new ClassPathResource("birthdays.xlsx");
        InputStream is = resource.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0); // First sheet
        rowIterator = sheet.iterator();
        rowIterator.next(); // Skip header row
    }

    @Override
    public Birthday read() {
        if (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Long sno = (long) row.getCell(0).getNumericCellValue();
            String name = getCellAsString(row.getCell(1));
            LocalDate birthDate = getDateFromCell(row.getCell(2));
            String email = getCellAsString(row.getCell(3));
            String contactNumber = getCellAsString(row.getCell(4));
            String deviceToken = getCellAsString(row.getCell(5));

            return new Birthday(sno,name, birthDate, email, contactNumber, deviceToken);
        }
        return null;
    }

    private String getCellAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private LocalDate getDateFromCell(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            try {
                String dateStr = getCellAsString(cell);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format in Excel cell: " + cell, e);
            }
        }
    }
}
