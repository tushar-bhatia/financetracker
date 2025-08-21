package com.finance.backend.validations;

import com.finance.backend.model.TransactionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, TransactionRequest> {
    @Override
    public boolean isValid(TransactionRequest value, ConstraintValidatorContext context) {
        if (value.startDate() == null || value.endDate() == null) {
            return true;
        }
        return !value.endDate().isBefore(value.startDate());
    }
}
