package com.finance.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

        @NotNull(message = "Id can't be null", groups = OnUpdate.class)
        @Positive(message = "Id must be positive", groups = OnUpdate.class)
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false,  updatable = false, insertable = false)
        Integer id;

        @NotNull(message = "Transaction date can't be null")
        @PastOrPresent(message = "Transaction date must be in the past or present")
        @Column(name = "transaction_date", nullable = false)
        LocalDate transactionDate;

        @NotNull(message = "Please provide a valid amount")
        Double amount;

        @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "category_id", nullable = false)
        Category category;

        @NotBlank(message = "Please provide a valid transaction type")
        @Pattern(regexp = "EXPENSE|INCOME", message = "Transaction type must either of type EXPENSE or INCOME")
        String transactionType;

        @NotBlank(message = "Please provide a valid description")
        String description;

        public Transaction() {
        }

        public Transaction(Integer id, LocalDate transactionDate, Double amount, Category category, String transactionType, String description) {
            this.id = id;
            this.transactionDate = transactionDate;
            this.amount = amount;
            this.category = category;
            this.transactionType = transactionType;
            this.description = description;
        }


    public Integer getId() {
        return id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDescription() { return description; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
