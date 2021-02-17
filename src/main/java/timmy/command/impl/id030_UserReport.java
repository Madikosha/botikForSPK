package timmy.command.impl;

import timmy.command.Command;
import timmy.entity.custom.Investments;
import timmy.entity.standart.User;
import timmy.utils.Const;
import timmy.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Slf4j
public class id030_UserReport extends Command {

    private List<Investments>  allInvestments;
    private List<User> allUser;
    private int         count;
    private int         messagePreviewReport;

    @Override
    public boolean execute()    throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (hasMessageText()) {
            count       = userDao.count();
            allInvestments    = investmentsDao.getAll();
            allUser = userDao.getAll();
            if (count == 0) {
                sendMessage(Const.REGISTRATION_USERS_NOT_FOUND_MESSAGE);
                return EXIT;
            }
            messagePreviewReport = sendMessage(String.format(getText(Const.USERS_REPORT_DOING_MESSAGE), count));
            new Thread(() -> {
                try {
                    sendReport();
                } catch (TelegramApiException e) {
                    log.error("Can't send report", e);
                    try {
                        sendMessage("Ошибка отправки списка");
                    } catch (TelegramApiException ex) {
                        log.error("Can't send message", ex);
                    }
                }
            }).start();
        }
        return COMEBACK;
    }

    private void        sendReport() throws TelegramApiException {
        int total           = count;
        XSSFWorkbook wb     = new XSSFWorkbook();
        Sheet sheets        = wb.createSheet("Пользователи");
        Sheet investments  = wb.createSheet("Инвестиции");
        // -------------------------Стиль ячеек-------------------------
        BorderStyle thin            = BorderStyle.THIN;
        short black                 = IndexedColors.BLACK.getIndex();
        XSSFCellStyle style         = wb.createCellStyle();
        style.setWrapText           (true);
        style.setAlignment          (HorizontalAlignment.CENTER);
        style.setVerticalAlignment  (VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop          (thin);
        style.setBorderBottom       (thin);
        style.setBorderRight        (thin);
        style.setBorderLeft         (thin);
        style.setTopBorderColor     (black);
        style.setRightBorderColor   (black);
        style.setBottomBorderColor  (black);
        style.setLeftBorderColor    (black);
        BorderStyle tittle              = BorderStyle.MEDIUM;
        XSSFCellStyle styleTitle        = wb.createCellStyle();
        styleTitle.setWrapText          (true);
        styleTitle.setAlignment         (HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment (VerticalAlignment.CENTER);
        styleTitle.setBorderTop         (tittle);
        styleTitle.setBorderBottom      (tittle);
        styleTitle.setBorderRight       (tittle);
        styleTitle.setBorderLeft        (tittle);
        styleTitle.setTopBorderColor    (black);
        styleTitle.setRightBorderColor  (black);
        styleTitle.setBottomBorderColor (black);
        styleTitle.setLeftBorderColor   (black);
        style.setFillForegroundColor    (new XSSFColor(new java.awt.Color(0, 52, 94)));
        Sheet sheet                     = wb.getSheetAt(0);
        //--------------------------------------------------------------------
        int rowIndex    = 0;
        int CellIndex   = 0;
        sheets  .createRow(rowIndex) .createCell(CellIndex)  .setCellValue("№");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("ФИО");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Контакты");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("E-mail");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Данные из телеграмма");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Название компании");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Отрасль вашей компании");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);

        for (User entity: allUser) {
            CellIndex = 0;
            sheets.createRow(++rowIndex).createCell(CellIndex).setCellValue(entity.getId());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getFullName());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getPhone());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getEmail());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getUserName());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getCompany());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getDepartment());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        }
//        Sheet sheet1                     = wb.getSheetAt(1);
//        int rowIndex1    = 1;
//        int CellIndex1  = 1;
//        sheets  .createRow(rowIndex1) .createCell(CellIndex1)  .setCellValue("№");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        sheets  .getRow(rowIndex1)    .createCell(++CellIndex1).setCellValue("ФИО");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        sheets  .getRow(rowIndex1)    .createCell(++CellIndex1).setCellValue("Контакты");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        sheets  .getRow(rowIndex1)    .createCell(++CellIndex1).setCellValue("E-mail");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle); sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Данные из телеграмма");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        sheets  .getRow(rowIndex1)    .createCell(++CellIndex1).setCellValue("Название компании");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        sheets  .getRow(rowIndex1)    .createCell(++CellIndex1).setCellValue("Отрасль вашей компании");
//        sheet   .getRow(rowIndex1)    .getCell(CellIndex1)     .setCellStyle(styleTitle);
//        for (User entity: allUser) {
//            CellIndex1 = 0;
//            investments.createRow(++rowIndex1).createCell(CellIndex1).setCellValue(entity.getId());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getFullName());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getPhone());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getEmail());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getUserName());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getCompany());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//            investments.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getDepartment());
//            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
//        }
        String[] splitWidth = "10000;10000;10000;10000;10000;10000;10000;10000".split(";");
        for (int i = 0; i < splitWidth.length; i++) {
            if (splitWidth[i].equalsIgnoreCase("auto")) {
                sheets       .autoSizeColumn(i);
//                sheet1.autoSizeColumn(i);
            } else {
                int size = 0;
                try {
                    size = Integer.parseInt(splitWidth[i].replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    log.warn("Error in message № 309 - {}", splitWidth[i]);
                }
                if (size > 0) {
                    sheets.setColumnWidth(i, size);
//                    sheet1.setColumnWidth(i,size);
                }
            }
        }
        String filename = String.format("Список пользователей %s.xlsx", DateUtil.getDayDate(new Date()));
        deleteMessage(messagePreviewReport);
        bot.execute(new SendDocument().setChatId(chatId).setDocument(filename, getInputStream(wb)));
    }

    private InputStream getInputStream(XSSFWorkbook workbook) {
        ByteArrayOutputStream tables = new ByteArrayOutputStream();
        try {
            workbook.write(tables);
        } catch (IOException e) {
            log.error("Can't write table to wb, case: {}", e);
        }
        return new ByteArrayInputStream(tables.toByteArray());
    }
}

