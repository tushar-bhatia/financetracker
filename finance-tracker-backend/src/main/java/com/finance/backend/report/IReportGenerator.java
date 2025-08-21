package com.finance.backend.report;

import com.finance.backend.model.FinanceSummary;
import com.finance.backend.model.Transaction;

import java.time.YearMonth;
import java.util.List;
import java.util.TreeMap;

public interface IReportGenerator {
    byte[] generateReport(List<Transaction> transactions, TreeMap<YearMonth, FinanceSummary> summary);
}
