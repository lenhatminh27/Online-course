package com.course.dao;

import com.course.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenDAO {
    void save(PasswordResetTokenEntity token);

    PasswordResetTokenEntity findByToken(String token);

    void delete(PasswordResetTokenEntity token);

    public boolean existsByToken(String token);
}
