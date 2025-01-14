package com.course.dao;

import com.course.entity.AccountEntity;

public interface AccountDAO {

    boolean existsByEmail(String email);

    AccountEntity findByEmail(String email);
}
