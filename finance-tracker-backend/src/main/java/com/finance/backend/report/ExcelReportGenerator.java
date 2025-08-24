package com.finance.backend.report;

import com.finance.backend.model.FinanceSummary;
import com.finance.backend.model.Transaction;
import com.finance.backend.util.ExcelUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ExcelReportGenerator implements IReportGenerator {

    @Override
    public byte[] generateReport(List<Transaction> transactions, TreeMap<YearMonth, FinanceSummary> summary) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Transactions Sheet
            XSSFSheet txSheet = ExcelUtils.addSheet(workbook, "All Transactions");
            createTransactionsSheet(txSheet, transactions);

            // Summary Sheet
            XSSFSheet summarySheet = ExcelUtils.addSheet(workbook,"Monthly Summary");
            createSummarySheet(summarySheet, summary);

            // Pie Chart for Summary
            XSSFSheet analysisSheet = ExcelUtils.addSheet(workbook, "Analysis");
            createPieChart(analysisSheet, summary);

            // Write to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private void createTransactionsSheet(XSSFSheet sheet, List<Transaction> transactions) {
        // Adding Headers
        String[] headers = {"Date", "Description", "Amount", "Category", "Transaction Type"};
        ExcelUtils.addHeaders(sheet, headers, true, 0);

        // Adding Transactions
        ExcelUtils.addTransactions(sheet, transactions);

        // Auto-size columns
        ExcelUtils.autoSizeColumns(sheet, headers.length);
    }

    private void createSummarySheet(XSSFSheet sheet, TreeMap<YearMonth, FinanceSummary> summary) {
        String[] headers = {"Period", "Income", "Expense", "Net Balance", "Category Expense", ""};
        String[] secondaryHeaders = {"", "", "", "", "Category", "Expense"};
        ExcelUtils.addHeaders(sheet, headers, true, 0);
        ExcelUtils.addHeaders(sheet, secondaryHeaders, false,1);

        // Merge Header Cells
        for(int i=0; i< headers.length-1; i++) {
            sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));

        // Data
        ExcelUtils.addMonthlySummary(sheet, summary);

        //auto-size columns
        ExcelUtils.autoSizeColumns(sheet, headers.length);
    }

    private void createPieChart(XSSFSheet sheet, TreeMap<YearMonth, FinanceSummary> summary) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 12, 20);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Income vs Expense Pie Chart");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        double totalIncome = 0;
        for (FinanceSummary fs : summary.values()) {
            totalIncome += fs.income();
        }
        Map<String, Double> categoryExpenses = summary.values().stream().map(s -> s.categoryExpense()).collect(Collectors.toList()).stream().flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(e -> e.getKey(), Collectors.summingDouble(e -> e.getValue())));
        String[] categoriesArray = new String[categoryExpenses.size() + 1];
        Double[] valuesArray = new Double[categoryExpenses.size() + 1];
        categoriesArray[0] = "INCOME";
        valuesArray[0] = totalIncome;
        int index = 1;
        for(Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            categoriesArray[index] = entry.getKey();
            valuesArray[index] = entry.getValue();
            index++;
        }

        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromArray(categoriesArray);
        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromArray(valuesArray);

        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData.Series series = data.addSeries(categories, values);
        series.setTitle("Summary", null);
        chart.plot(data);
    }
}
