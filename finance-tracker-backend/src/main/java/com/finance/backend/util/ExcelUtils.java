package com.finance.backend.util;

import com.finance.backend.model.FinanceSummary;
import com.finance.backend.model.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelUtils {

    private static XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static XSSFCellStyle createDataStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    public static XSSFSheet addSheet(XSSFWorkbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    public static void addHeaders(XSSFSheet sheet, String[] headers, boolean includeSerialNumber, int headerRowIndex) {
        XSSFCellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        Row headerRow = sheet.createRow(headerRowIndex);
        if(includeSerialNumber) setSerialNumberHeader(headerRow, headerStyle);
        for (int i = 0; i <headers.length; i++) {
            Cell cell = headerRow.createCell(i+1);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void setSerialNumberHeader(Row headerRow, XSSFCellStyle headerStyle) {
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("SR.No");
        cell.setCellStyle(headerStyle);
    }

    private static void setSerialNumberData(Row row, int index, XSSFCellStyle dataStyle) {
        Cell cell = row.createCell(0);
        cell.setCellValue(index);
        cell.setCellStyle(dataStyle);
    }

    public static void autoSizeColumns(XSSFSheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns+1; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void addTransactionRow(XSSFSheet sheet, Transaction transaction, int rowNumber, XSSFCellStyle dataStyle) {
        String[] transactionData = TransactionUtils.parseTransactionData(transaction);
        Row row = sheet.createRow(rowNumber);
        setSerialNumberData(row, rowNumber, dataStyle);
        for (int i = 0; i < transactionData.length; i++) {
            Cell cell = row.createCell(i+1);
            cell.setCellValue(transactionData[i]);
            cell.setCellStyle(dataStyle);
        }
    }

    public static void addTransactions(XSSFSheet sheet, List<Transaction> transactions) {
        XSSFCellStyle dataStyle = createDataStyle(sheet.getWorkbook());
        int i=1;
        for(Transaction transaction : transactions){
            addTransactionRow(sheet, transaction, i++, dataStyle);
        }
    }

    public static void addMonthlySummary(XSSFSheet sheet, Map<YearMonth, FinanceSummary> summaries) {
        int i=1;
        int counter=1;
        for(YearMonth yearMonth : summaries.keySet()) {
            FinanceSummary summary = summaries.get(yearMonth);
            i = addSummary(sheet, i+1, summary, yearMonth, counter++);
        }
    }

    private static int addSummary(XSSFSheet sheet, int rowNumber, FinanceSummary summary, YearMonth yearMonth, int SerialNumber) {
        XSSFCellStyle dataStyle = createDataStyle(sheet.getWorkbook());

        Row row = sheet.createRow(rowNumber);
        setSerialNumberData(row, SerialNumber, dataStyle);

        Cell monthCell = row.createCell(1);
        monthCell.setCellValue(yearMonth.toString());
        monthCell.setCellStyle(dataStyle);

        Cell incomeCell = row.createCell(2);
        incomeCell.setCellValue(summary.income());
        incomeCell.setCellStyle(dataStyle);

        Cell expenseCell = row.createCell(3);
        expenseCell.setCellValue(summary.expense());
        expenseCell.setCellStyle(dataStyle);

        Cell balanceCell = row.createCell(4);
        balanceCell.setCellValue(summary.balance());
        balanceCell.setCellStyle(dataStyle);

        Set<Map.Entry<String, Double>> categoryExpenses = summary.categoryExpense().entrySet();
        int counter=0;
        Row endRow = row;
        for(Map.Entry<String, Double> entry : categoryExpenses) {
            if(counter != 0) {
                endRow = sheet.createRow(++rowNumber);
                createEmptyCells(endRow, 5, dataStyle);
            }
            Cell categoryCell = endRow.createCell(5);
            categoryCell.setCellValue(entry.getKey());
            categoryCell.setCellStyle(dataStyle);

            Cell expenseValueCell = endRow.createCell(6);
            expenseValueCell.setCellValue(entry.getValue());
            expenseValueCell.setCellStyle(dataStyle);
            counter++;
        }
        for(int i=0; i<5 && categoryExpenses.size()>1; i++) {
            sheet.addMergedRegion(new CellRangeAddress(endRow.getRowNum()-categoryExpenses.size()+1, endRow.getRowNum(), i, i));
        }
        return rowNumber;
    }

    private static void createEmptyCells(Row row, int numOfEmptyCells, XSSFCellStyle style) {
        for (int i = 0; i < numOfEmptyCells; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(style);
        }
    }
}
