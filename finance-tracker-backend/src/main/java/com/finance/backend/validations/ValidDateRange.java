package com.finance.backend.validations;

import com.finance.backend.model.OnFilter;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ ElementType.TYPE })   // class-level validation
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

    String message() default "End date must be greater than or equal to start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
