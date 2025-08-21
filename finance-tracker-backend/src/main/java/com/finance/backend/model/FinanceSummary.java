package com.finance.backend.model;

import java.util.Map;

public record FinanceSummary(Double income, Double expense, Double balance, Map<String, Double> categoryExpense) { }
