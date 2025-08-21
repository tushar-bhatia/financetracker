package com.finance.backend.dao;

import com.finance.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaSpecificationExecutor<Transaction>, JpaRepository<Transaction, Integer> {
}
