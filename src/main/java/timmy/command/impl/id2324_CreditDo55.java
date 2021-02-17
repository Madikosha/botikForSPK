package timmy.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.utils.Const;
import timmy.utils.DateUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class id2324_CreditDo55 extends Command {
    private int credit;
    private int percent;
    private int months;
    private double res;
    private int platezh;

    private int         count;
    private int         messagePreviewReport;

    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType){
            case START:
                sendMessage(10000004);
                waitingType = waitingType.NEW_EVENT;
                return COMEBACK;
            case NEW_EVENT:
                credit = Integer.parseInt(updateMessageText);
                if(credit>=20000000 && credit<=500000000){
                    sendMessage(10000003);
                    waitingType = waitingType.EXAMPLE;
                }else{
                    wrongData();
                }
                return COMEBACK;
            case EXAMPLE:
                percent = Integer.parseInt(update.getCallbackQuery().getData().replaceAll("[^0-9]", ""));
                sendMessage(10000001);
                waitingType = waitingType.NEW_QUEST;
                return COMEBACK;
            case NEW_QUEST:
                if (hasMessageText()){
                    System.out.println(percent);
                    months = Integer.parseInt(updateMessageText);
                    if (months>=3 && months<=84) {
                        res = (credit * (((double) percent / 100 / 12) * Math.pow((1 + (double) percent / 100 / 12), months))) / (Math.pow((1 + (double) percent / 100 / 12), months) - 1);
                        platezh = (int) res;
                        System.out.println();
                        String text = String.format(getText(10000002), String.valueOf(platezh));
                        sendMessage(text);
                        execute1();
                    }
                }else {
                    wrongData();
                }
                return COMEBACK;
        }
        return EXIT;
    }
    public int getMonths(){
        months = Integer.parseInt(updateMessageText);
        return months;
    }
    private int     wrongData()     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    public boolean execute1()    throws TelegramApiException {
        if (hasMessageText()) {
            count       = getMonths();
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
        Sheet sheets        = wb.createSheet("Кредитный калькулятор");
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
        style.setFillForegroundColor    (new XSSFColor(new java.awt.Color(0, 52, 100)));
        Sheet sheet                     = wb.getSheetAt(0);
        //--------------------------------------------------------------------
        int nachis = (int) (credit * percent / 100 / 12);
        int sum = platezh - nachis;
        System.out.println(platezh);
        System.out.println(nachis);
        System.out.println(sum);

        int rowIndex1    = 0;
        int CellIndex1   = 0;
        int rowIndex2    = 1;
        int CellIndex2   = 0;
        int rowIndex3    = 2;
        int CellIndex3   = 0;
        int rowIndex4    = 3;
        int CellIndex4   = 0;
        int rowIndex5    = 4;
        int CellIndex5   = 0;
        int rowIndex6    = 5;
        int CellIndex6   = 0;

        sheets  .createRow(rowIndex6) .createCell(CellIndex6)  .setCellValue("Сумма кредита");
        sheet   .getRow(rowIndex6)    .getCell(CellIndex6)     .setCellStyle(styleTitle);
        sheets  .createRow(rowIndex2)    .createCell(CellIndex2).setCellValue("Срок кредита");
        sheet   .getRow(rowIndex2)    .getCell(CellIndex2)     .setCellStyle(styleTitle);
        sheets  .createRow(rowIndex3)    .createCell(CellIndex3).setCellValue("Процентная ставка");
        sheet   .getRow(rowIndex3)    .getCell(CellIndex3)     .setCellStyle(styleTitle);
        sheets  .createRow(rowIndex4)    .createCell(CellIndex4).setCellValue("Общая сумма выплат");
        sheet   .getRow(rowIndex4)    .getCell(CellIndex4)     .setCellStyle(styleTitle);
        sheets  .createRow(rowIndex5)    .createCell(CellIndex5).setCellValue("Общая сумма %");
        sheet   .getRow(rowIndex5)    .getCell(CellIndex5)     .setCellStyle(styleTitle);

        CellIndex1 = 1;
        sheets.createRow(++rowIndex6).createCell(++CellIndex6).setCellValue(credit);
        sheet.getRow(rowIndex6).getCell(CellIndex6).setCellStyle(style);
        sheets.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(months);
        sheet.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
        sheets.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(percent + " %");
        sheet.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
        sheets.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(months*platezh);
        sheet.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
        sheets.getRow(rowIndex5).createCell(++CellIndex5).setCellValue((months*platezh)-credit);
        sheet.getRow(rowIndex5).getCell(CellIndex5).setCellStyle(style);

        int rowIndex    = 7;
        int CellIndex   = 0;
        sheets  .createRow(rowIndex) .createCell(CellIndex)  .setCellValue("Месяц");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Основной долг");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Начисленные %");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Сумма основного долга");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);
        sheets  .getRow(rowIndex)    .createCell(++CellIndex).setCellValue("Платёж");
        sheet   .getRow(rowIndex)    .getCell(CellIndex)     .setCellStyle(styleTitle);

        CellIndex = 0;
        sheets.createRow(++rowIndex).createCell(CellIndex).setCellValue(1);
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(credit);
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(nachis = (int) (credit * percent / 100 / 12));
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(sum = platezh-nachis);
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(platezh);
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        for (int i = 2; i<=months; i++) {
            CellIndex = 0;
            sheets.createRow(++rowIndex).createCell(CellIndex).setCellValue(i);
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(credit = credit-sum);
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(nachis = (int) (credit * percent / 100 / 12));
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(sum = platezh-nachis);
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(platezh);
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        }

        String[] splitWidth = "10000;10000;10000;10000;10000;".split(";");
        for (int i = 0; i < splitWidth.length; i++) {
            if (splitWidth[i].equalsIgnoreCase("auto")) {
                sheets       .autoSizeColumn(i);
            } else {
                int size = 0;
                try {
                    size = Integer.parseInt(splitWidth[i].replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    log.warn("Error in message № 309 - {}", splitWidth[i]);
                }
                if (size > 0) {
                    sheets.setColumnWidth(i, size);
                }
            }
        }
        String filename = String.format("Предварительный график оплат.xlsx", DateUtil.getDayDate(new Date()));
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
