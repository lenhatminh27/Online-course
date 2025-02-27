package com.course.dao;

import com.course.entity.WalletEntity;

public interface WalletDAO {

    WalletEntity createWallet(WalletEntity wallet);

    WalletEntity getWallet(Long id);

    WalletEntity updateWallet(WalletEntity wallet);
}
