package com.course.dao;

import com.course.entity.TransactionEntity;

public interface TransactionDAO {
    TransactionEntity saveTransaction(TransactionEntity transaction);

    TransactionEntity findById(Long tranId);

    TransactionEntity update(TransactionEntity transaction);
}
