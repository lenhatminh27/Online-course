package com.course.dao;

import com.course.entity.AccountEntity;
import com.course.entity.AccountProfileEntity;

public interface AccountDAO {

    boolean existsByEmail(String email);

    AccountEntity findByEmail(String email);

    AccountEntity save(AccountEntity account);

    AccountEntity update(AccountEntity account);

    AccountProfileEntity saveAccountProfile(AccountProfileEntity accountProfile);

    public boolean isValidEmail(String email);

}
