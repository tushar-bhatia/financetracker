package com.finance.backend.model;

import com.finance.backend.validations.ValidDateRange;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@ValidDateRange(groups = OnFilter.class)
public record TransactionRequest(

        @NotNull(message = "Transaction Id can't be null", groups = OnUpdate.class)
        @Positive(message = "Transaction Id should be greater than zero.", groups = OnUpdate.class)
        Integer id,

        @Positive(message = "Category Id must be positive", groups = {OnCreate.class, OnUpdate.class, OnFilter.class})
        @NotNull(message = "Category Id can't be null", groups = {OnCreate.class, OnUpdate.class})
        Integer categoryId,

        @NotNull(message = "Transaction type can't be null", groups = {OnCreate.class, OnUpdate.class})
        @Pattern(regexp = "EXPENSE|INCOME", message = "Transaction type must either of type EXPENSE or INCOME", groups = {OnCreate.class, OnUpdate.class, OnFilter.class})
        String transactionType,

        @PastOrPresent(message = "Start date can either be past or present", groups = OnFilter.class)
        LocalDate startDate,

        @PastOrPresent(message = "End date can either be past or present", groups = OnFilter.class)
        LocalDate endDate,

        @NotNull(message = "Transaction date can't be null", groups = {OnCreate.class, OnUpdate.class})
        @PastOrPresent(message = "Transaction date can either be past or present", groups = {OnCreate.class, OnUpdate.class})
        LocalDate transactionDate,

        @NotNull(message = "Kindly provide a valid amount", groups = {OnCreate.class, OnUpdate.class})
        @Positive(message = "Amount should be positive", groups = {OnCreate.class, OnUpdate.class})
        Double amount,

        @NotBlank(message = "Kindly provide a valid message", groups = {OnCreate.class, OnUpdate.class})
        String description) { }
