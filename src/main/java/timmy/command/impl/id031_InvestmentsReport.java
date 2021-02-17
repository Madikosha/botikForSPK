package timmy.command.impl;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import timmy.command.Command;
import timmy.config.Bot;
import timmy.dao.DaoFactory;
import timmy.entity.custom.*;
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
import java.util.Objects;

@Slf4j
public class id031_InvestmentsReport extends Command {

    private List<Application> allApplication;
    private List<Suggestion> allSuggestion;
    private List<Operators> allOperators;
    private List<Complaint> allComplaint;
    private List<Investments> allInvestments;

    private int count;
    private int messagePreviewReport;
    private DaoFactory daoFactory = new DaoFactory();

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (hasMessageText()) {
            count = applicationDao.count();
            allApplication = applicationDao.getAll();
            allSuggestion = suggestionDao.getAll();
            allInvestments = investmentsDao.getAll();
            allOperators = operatorsDao.getAll();
            allComplaint = complaintDao.getAll();
            if (count == 0) {
                sendMessage(Const.REGISTRATION_USERS_NOT_FOUND_MESSAGE);
                return EXIT;
            }
            messagePreviewReport = sendMessage((Const.USERS_REPORT_DOING_MESSAGE));
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

    private void sendReport() throws TelegramApiException {
        int total = count;
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheets = wb.createSheet("Оставить заявку");
        Sheet sheets1 = wb.createSheet("Предложить проект");
        Sheet sheets2 = wb.createSheet("Инвестировать в проекты");
        Sheet sheets3 = wb.createSheet("Получить консультацию");
        Sheet sheets4 = wb.createSheet("Обратная связь");

        // -------------------------Стиль ячеек-------------------------
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();
        XSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(thin);
        style.setBorderBottom(thin);
        style.setBorderRight(thin);
        style.setBorderLeft(thin);
        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);
        BorderStyle tittle = BorderStyle.MEDIUM;
        XSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setWrapText(true);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setBorderTop(tittle);
        styleTitle.setBorderBottom(tittle);
        styleTitle.setBorderRight(tittle);
        styleTitle.setBorderLeft(tittle);
        styleTitle.setTopBorderColor(black);
        styleTitle.setRightBorderColor(black);
        styleTitle.setBottomBorderColor(black);
        styleTitle.setLeftBorderColor(black);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 52, 94)));


        //--------------------------------------------------------------------
        Sheet sheet = wb.getSheetAt(0);
        int rowIndex = 0;
        int CellIndex = 0;
        sheets.createRow(rowIndex).createCell(CellIndex).setCellValue("№");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Категория");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("ФИО");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Телефон");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Email");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Компания");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Отрасль");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Вопрос");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);
        sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Комментарии");
        sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(styleTitle);

        for (Application entity : allApplication) {
            CellIndex = 0;
            sheets.createRow(++rowIndex).createCell(CellIndex).setCellValue(entity.getId());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            if (entity.getLevelId() == 1) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Активы СПК");
            } else if (entity.getLevelId() == 2) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Ярмарки");
            } else if (entity.getLevelId() == 3) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Кредит МФО Almaty");
            } else if (entity.getLevelId() == 4) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Кредит Almaty Finance");
            } else if (entity.getLevelId() == 5) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Индустриальная зона");
            } else if (entity.getLevelId() == 6) {
                sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue("Промпарки");
            }
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getFullName());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getPhoneNumber());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getEmail());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getCompany());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getDepartment());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getRequest());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
            sheets.getRow(rowIndex).createCell(++CellIndex).setCellValue(entity.getComment());
            sheet.getRow(rowIndex).getCell(CellIndex).setCellStyle(style);
        }

        Sheet sheet1 = wb.getSheetAt(1);
        int rowIndex1 = 0;
        int CellIndex1 = 0;
        sheets1.createRow(rowIndex1).createCell(CellIndex1).setCellValue("№");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("ФИО");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Телефон");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Email");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Компания");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Отрасль");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Вопрос");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Комментарии");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);
        sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("Файл(отметка о приложении");
        sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(styleTitle);

        for (Suggestion entity : allSuggestion) {
            CellIndex1 = 0;
            sheets1.createRow(++rowIndex1).createCell(CellIndex1).setCellValue(entity.getId());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getFullName());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getPhoneNumber());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getEmail());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getCompany());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getDepartment());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getDescription());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue(entity.getComment());
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
            if(entity.getFile() == null){
                sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("");
            } else sheets1.getRow(rowIndex1).createCell(++CellIndex1).setCellValue("https://api.telegram.org/file/bot" + propertiesDao.getPropertiesValue(Const.BOT_TOKEN) + "/" + uploadFile(entity.getFile()));
            sheet1.getRow(rowIndex1).getCell(CellIndex1).setCellStyle(style);
        }
        Sheet sheet2 = wb.getSheetAt(2);
        int rowIndex2 = 0;
        int CellIndex2 = 0;
        sheets2.createRow(rowIndex2).createCell(CellIndex2).setCellValue("№");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("ФИО");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Телефон");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Email");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Компания");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Отрасль");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Вопрос");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);
        sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue("Комментарии");
        sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(styleTitle);

        for (Investments entity : allInvestments) {
            CellIndex2 = 0;
            sheets2.createRow(++rowIndex2).createCell(CellIndex2).setCellValue(entity.getId());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getFullName());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getContact());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getEmail());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getCompany());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getDepartment());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getText());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
            sheets2.getRow(rowIndex2).createCell(++CellIndex2).setCellValue(entity.getComment());
            sheet2.getRow(rowIndex2).getCell(CellIndex2).setCellStyle(style);
        }
        Sheet sheet3 = wb.getSheetAt(3);
        int rowIndex3 = 0;
        int CellIndex3 = 0;
        sheets3.createRow(rowIndex3).createCell(CellIndex3).setCellValue("№");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Категория");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("ФИО");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Телефон");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Email");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Компания");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Отрасль");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);
        sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Вопрос");
        sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(styleTitle);

        for (Operators entity : allOperators) {
            CellIndex3 = 0;
            sheets3.createRow(++rowIndex3).createCell(CellIndex3).setCellValue(entity.getId());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            if (entity.getLevelId() == 1) {
                sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Активы СПК");
            } else if (entity.getLevelId() == 2) {
                sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("НОТы(участок для торговли)");
            } else if (entity.getLevelId() == 3) {
                sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Ярмарки");
            } else if (entity.getLevelId() == 4) {
                sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue("Промпарки");
            }
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getFullName());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getPhoneNumber());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getEmail());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getCompany());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getDepartment());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
            sheets3.getRow(rowIndex3).createCell(++CellIndex3).setCellValue(entity.getQuestion());
            sheet3.getRow(rowIndex3).getCell(CellIndex3).setCellStyle(style);
        }

        Sheet sheet4 = wb.getSheetAt(4);
        int rowIndex4 = 0;
        int CellIndex4 = 0;
        sheets4.createRow(rowIndex4).createCell(CellIndex4).setCellValue("№");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Категория");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("ФИО");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Телефон");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Email");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Компания");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Отрасль");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);
        sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue("Вопрос/Предложение");
        sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(styleTitle);

        for (Complaint entity : allComplaint) {
            CellIndex4 = 0;
            sheets4.createRow(++rowIndex4).createCell(CellIndex4).setCellValue(entity.getId());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getCategory());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getFullName());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getContact());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getEmail());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getCompany());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getDepartment());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
            sheets4.getRow(rowIndex4).createCell(++CellIndex4).setCellValue(entity.getText());
            sheet4.getRow(rowIndex4).getCell(CellIndex4).setCellStyle(style);
        }

        String[] splitWidth = "10000;10000;10000;10000;10000;10000;10000;10000;10000".split(";");
        for (int i = 0; i < splitWidth.length; i++) {
            if (splitWidth[i].equalsIgnoreCase("auto")) {
                sheets.autoSizeColumn(i);
                sheets1.autoSizeColumn(i);
                sheets2.autoSizeColumn(i);
                sheets3.autoSizeColumn(i);
                sheets4.autoSizeColumn(i);
            } else {
                int size = 0;
                try {
                    size = Integer.parseInt(splitWidth[i].replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    log.warn("Error in message № 309 - {}", splitWidth[i]);
                }
                if (size > 0) {
                    sheets.setColumnWidth(i, size);
                    sheets1.setColumnWidth(i, size);
                    sheets2.setColumnWidth(i, size);
                    sheets3.setColumnWidth(i, size);
                    sheets4.setColumnWidth(i, size);
                }
            }
        }
        String filename = String.format("Отчеты %s.xlsx", DateUtil.getDayDate(new Date()));
        deleteMessage(messagePreviewReport);
        bot.execute(new SendDocument().setChatId(chatId).setDocument(filename, getInputStream(wb)));
    }

    private String uploadFile(String fileId) {
//        Bot bot = new Bot();
        Objects.requireNonNull(fileId);
        GetFile getFile = new GetFile().setFileId(fileId);
        try {
            File file = bot.execute(getFile);
            return file.getFilePath();
        } catch (TelegramApiException e) {
            throw new IllegalMonitorStateException();
        }
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



