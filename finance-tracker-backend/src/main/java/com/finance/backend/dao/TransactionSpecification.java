package com.finance.backend.dao;

import com.finance.backend.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class TransactionSpecification {
    public static Specification<Transaction> hasCategory(Integer categoryId) {
        return (root, criteriaQuery, criteriaBuilder) -> categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Transaction> hasTransactionType(String transactionType) {
        return (root, criteriaQuery, criteriaBuilder) -> transactionType == null ? null : criteriaBuilder.equal(root.get("transactionType"), transactionType);
    }

    public static Specification<Transaction> hasDates(LocalDate startDate, LocalDate endDate) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("transactionDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate);
            }
            return null;
        };
    }
}
