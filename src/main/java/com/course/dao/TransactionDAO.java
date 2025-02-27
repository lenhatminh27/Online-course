package com.course.dao;

import com.course.entity.TransactionEntity;

public interface TransactionDAO {
    TransactionEntity saveTransaction(TransactionEntity transaction);

    TransactionEntity findByTransactionDescription(String desc);

    TransactionEntity update(TransactionEntity transaction);
}
