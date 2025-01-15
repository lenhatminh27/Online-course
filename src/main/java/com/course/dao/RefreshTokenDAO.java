package com.course.dao;

import com.course.entity.AccountEntity;
import com.course.entity.RefreshTokenEntity;

import java.util.List;

public interface RefreshTokenDAO {

    RefreshTokenEntity save(RefreshTokenEntity refreshToken);

    RefreshTokenEntity update(RefreshTokenEntity refreshToken);

    List<RefreshTokenEntity> findByAccountAndRevoked(AccountEntity account, boolean isRevoked);

    void saveAll(List<RefreshTokenEntity> listRefreshTokenAccount);

    RefreshTokenEntity findByRefreshTokenAnsRevoked(String refreshToken, boolean isRevoked);

}
