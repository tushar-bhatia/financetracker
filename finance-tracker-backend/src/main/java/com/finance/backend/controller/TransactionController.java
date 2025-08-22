package com.finance.backend.controller;

import com.finance.backend.model.*;
import com.finance.backend.service.ITransactionService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/financetracker/transaction/api/v1")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    @Qualifier("transactionServiceImpl")
    ITransactionService transactionService;

    @PostMapping("/getAll")
    public ResponseEntity<?> getAllTransactions(@Validated(OnFilter.class) @RequestBody(required = false) TransactionRequest transactionRequest) {
        LOGGER.info("Request received to display transactions");
        try {
            List<Transaction> transactions = transactionService.displayTransactions(transactionRequest);
            LOGGER.info("Request completed for display transactions");
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch(Exception e) {
            LOGGER.error("Error occurred while getting transactions", e);
            throw e;
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTransactionById(@NotNull(message = "id can't be null")
                                                    @Positive(message = "id must be positive")
                                                    @PathVariable("id") int id) {
        LOGGER.info("Request received to get a transaction with id {}", id);
        try {
            Transaction transaction = transactionService.findTransactionById(id);
            LOGGER.info("Transaction fetched successfully with id {}", id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while fetching transaction with id {}", id, e);
            throw e;
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@Validated(OnCreate.class) @RequestBody TransactionRequest transactionRequest) {
        LOGGER.info("Request received to add new transaction");
        try {
            Transaction savedTransaction = transactionService.addTransaction(transactionRequest);
            LOGGER.info("New transaction added successfully with id {}", savedTransaction.getId());
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while adding transaction", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@NotNull(message = "id can't be null")
                                                   @Positive(message = "id must be positive")
                                                @PathVariable("id") int id) {
        LOGGER.info("Request received to delete transaction with id {}", id);
        try {
            transactionService.deleteTransaction(id);
            LOGGER.info("Transaction deleted successfully with id {}", id);
            return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while deleting transaction with id {}", id, e);
            throw e;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateTransaction(@Validated(OnUpdate.class) @RequestBody TransactionRequest transactionRequest) {
        LOGGER.info("Request received to edit transaction with id {}", transactionRequest.id());
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(transactionRequest);
            LOGGER.info("Transaction updated successfully with id {}", updatedTransaction.getId());
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            LOGGER.error("Error occurred while updating transaction with id {}", transactionRequest.id(), e);
            throw e;
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getMonthlyTransactionSummary(@Validated(OnFilter.class) @RequestBody(required = false) TransactionRequest transactionRequest) {
        LOGGER.info("Request received to display monthly transaction summary");
        try {
            List<Transaction> transactions = transactionService.displayTransactions(transactionRequest);
            if (transactions.isEmpty()) {
                LOGGER.info("No transactions found for the given criteria");
                return new ResponseEntity<>("No transactions found", HttpStatus.NO_CONTENT);
            }
            Map<YearMonth, FinanceSummary> monthlySummary = transactionService.getMonthlySummary(transactions);
            LOGGER.info("Request completed for display monthly transaction summary");
            return new ResponseEntity<>(monthlySummary, HttpStatus.OK);
        } catch(Exception e) {
            LOGGER.error("Error occurred while curating monthly transaction summary", e);
            throw e;
        }
    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadMonthlySummaryExcelReport(@Validated(OnFilter.class) @RequestBody(required = false) TransactionRequest transactionRequest) {
        LOGGER.info("Request received to download monthly transaction summary as Excel report");
        try {
            byte[] excelReport = transactionService.downloadMonthlySummaryExcelReport(transactionRequest);
            LOGGER.info("Excel report generated successfully");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=transactions_report.xlsx")
                    .body(excelReport);
        } catch(Exception e) {
            LOGGER.error("Error occurred while generating Excel report", e);
            throw e;
        }
    }
}
