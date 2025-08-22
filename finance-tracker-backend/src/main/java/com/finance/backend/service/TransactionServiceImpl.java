package com.finance.backend.service;

import com.finance.backend.dao.CategoryRepository;
import com.finance.backend.dao.SpecificationBuilder;
import com.finance.backend.dao.TransactionRepository;
import com.finance.backend.dao.TransactionSpecification;
import com.finance.backend.model.*;
import com.finance.backend.report.IReportGenerator;
import com.finance.backend.util.TransactionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    @Qualifier("transactionRepository")
    TransactionRepository transactionRepository;

    @Autowired
    @Qualifier("categoryRepository")
    CategoryRepository categoryRepository;

    @Autowired
    @Qualifier("excelReportGenerator")
    IReportGenerator reportGenerator;

    @Override
    public List<Transaction> displayTransactions(TransactionRequest transactionRequest) {
        if(transactionRequest == null) return transactionRepository.findAll(Sort.by(Sort.Direction.ASC, "transactionDate"));
        SpecificationBuilder<Transaction> transactionSpecificationBuilder = new SpecificationBuilder<>();
        Specification<Transaction> transactionSpecifications = transactionSpecificationBuilder
                .add(TransactionSpecification.hasTransactionType(transactionRequest.transactionType()))
                .add(TransactionSpecification.hasCategory(transactionRequest.categoryId()))
                .add(TransactionSpecification.hasDates(transactionRequest.startDate(), transactionRequest.endDate()))
                .build();
        List<Transaction> transactions = transactionRepository.findAll(transactionSpecifications, Sort.by(Sort.Direction.DESC, "transactionDate"));
        LOGGER.info("Total trsanctions found: {}", transactions.size());
        return transactions;
    }

    @Override
    public Transaction addTransaction(TransactionRequest transactionRequest) {
        Optional<Category> categoryOptional = categoryRepository.findById(transactionRequest.categoryId());
        if(categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Please provide a valid category value.");
        }
        Transaction transaction = TransactionUtils.getTransaction(transactionRequest, categoryOptional.get());
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findTransactionById(int id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new IllegalArgumentException("Transaction with id " + id + " does not exist");
        }
        return transaction.get();
    }

    @Override
    public void deleteTransaction(int id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction with id " + id + " does not exist");
        }
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction updateTransaction(TransactionRequest transactionRequest) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionRequest.id());
        if (transactionOptional.isEmpty()) {
            throw new IllegalArgumentException("Transaction with id " + transactionRequest.id() + " does not exist");
        }
        Optional<Category> categoryOptional = categoryRepository.findById(transactionRequest.categoryId());
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Please provide a valid category value.");
        }
        Transaction newTransaction = TransactionUtils.getTransaction(transactionRequest, categoryOptional.get());
        return transactionRepository.save(newTransaction);
    }

    @Override
    public TreeMap<YearMonth, FinanceSummary> getMonthlySummary(List<Transaction> transactions) {
        TreeMap<YearMonth, FinanceSummary> monthlySummary = new TreeMap<>();
        Map<YearMonth, List<Transaction>> yearMonthListMap = transactions.stream().collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate())));
        double runningBalance = 0.0;
        for(YearMonth yearMonth : yearMonthListMap.keySet().stream().sorted().toList()) {
            List<Transaction> monthlyTransactions = yearMonthListMap.get(yearMonth);
            double totalIncome = monthlyTransactions.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase(TransactionType.INCOME.name()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            double totalExpense = monthlyTransactions.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase(TransactionType.EXPENSE.name()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            Map<String, Double> categoryWiseExpense = monthlyTransactions.stream().filter(t -> t.getTransactionType().equalsIgnoreCase(TransactionType.EXPENSE.name()))
                    .collect(Collectors.groupingBy(t -> t.getCategory().getName(), Collectors.summingDouble(Transaction::getAmount)));
            runningBalance = totalIncome - totalExpense + runningBalance;

            FinanceSummary summary = new FinanceSummary(totalIncome, totalExpense, runningBalance, categoryWiseExpense);
            monthlySummary.put(yearMonth, summary);
        }
        return monthlySummary;
    }

    @Override
    public byte[] downloadMonthlySummaryExcelReport(TransactionRequest transactionRequest) {
        List<Transaction> transactions = displayTransactions(transactionRequest);
        if(transactions.isEmpty()) {
            throw new IllegalArgumentException("No transactions found for the given criteria.");
        }
        TreeMap<YearMonth, FinanceSummary> monthlySummary = getMonthlySummary(transactions);
        return reportGenerator.generateReport(transactions, monthlySummary);
    }

}
