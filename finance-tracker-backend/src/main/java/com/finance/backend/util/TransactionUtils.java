package com.finance.backend.util;

import com.finance.backend.model.Category;
import com.finance.backend.model.Transaction;
import com.finance.backend.model.TransactionRequest;

public class TransactionUtils {

    private TransactionUtils() {
        // Private constructor to prevent instantiation
    }

    public static Transaction getTransaction(TransactionRequest transactionRequest, Category category) {
        return new Transaction(
                transactionRequest.id(), transactionRequest.transactionDate(), transactionRequest.amount(),
                category, transactionRequest.transactionType(), transactionRequest.description()
        );
    }

    public static String[] parseTransactionData(Transaction transaction) {
        String[] data = new String[5];
        data[0] = transaction.getTransactionDate().toString();
        data[1] = transaction.getDescription();
        data[2] = String.valueOf(transaction.getAmount());
        data[3] = transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A";
        data[4] = transaction.getTransactionType() != null ? transaction.getTransactionType() : "N/A";
        return data;
    }
}
