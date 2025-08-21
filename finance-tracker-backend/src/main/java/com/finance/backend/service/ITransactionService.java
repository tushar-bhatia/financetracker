package com.finance.backend.service;

import com.finance.backend.model.FinanceSummary;
import com.finance.backend.model.Transaction;
import com.finance.backend.model.TransactionRequest;

import java.time.YearMonth;
import java.util.List;
import java.util.TreeMap;

public interface ITransactionService {
    List<Transaction> displayTransactions(TransactionRequest transactionRequest);
    Transaction addTransaction(TransactionRequest transactionRequest);
    Transaction findTransactionById(int id);
    void deleteTransaction(int id);
    Transaction updateTransaction(TransactionRequest transactionRequest);
    TreeMap<YearMonth, FinanceSummary> getMonthlySummary(List<Transaction> transactions);
    byte[] downloadMonthlySummaryExcelReport(TransactionRequest transactionRequest);

}
