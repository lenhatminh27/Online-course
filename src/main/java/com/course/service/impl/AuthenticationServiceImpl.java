package com.course.service.impl;

import com.course.common.utils.MessageUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.dto.request.AuthenticationRequest;
import com.course.dto.response.AuthenticationResponse;
import com.course.entity.AccountEntity;
import com.course.entity.RefreshTokenEntity;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
import com.course.exceptions.AuthenticationException;
import com.course.security.TokenProvider;
import com.course.security.context.AuthenticationContext;
import com.course.service.AuthenticationService;

import java.util.List;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenProvider tokenProvider;

    private final AccountDAO authenticationDAO;

    private final RefreshTokenDAO refreshTokenDAO;

    public AuthenticationServiceImpl(TokenProvider tokenProvider, AccountDAO authenticationDAO, RefreshTokenDAO refreshTokenDAO) {
        this.tokenProvider = tokenProvider;
        this.authenticationDAO = authenticationDAO;
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        if(!authenticationDAO.existsByEmail(authenticationRequest.getEmail())) {
            throw new AuthenticationException(MessageUtils.LOGIN_FAIL);
        }
        AccountEntity account = authenticationDAO.findByEmail(authenticationRequest.getEmail());
        if(!account.getPasswordHash().equals(authenticationRequest.getPassword())) {
            throw new AuthenticationException(MessageUtils.LOGIN_FAIL);
        }
        AuthenticationContext context = new AuthenticationContext();
        context.setEmail(account.getEmail());
        List<String> authorities = account.getRoles().stream().map(RoleEntity::getName)
                .map(ERole::name).toList();
        context.setAuthorities(authorities);
        String accessToken = tokenProvider.createAccessToken(context);
        String refreshToken = tokenProvider.createRefreshToken(context, authenticationRequest.isRememberMe());

        //revoked refresh token
        updateRevokedRefreshToken(account);

        //save refresh token
        saveRefreshToken(account, refreshToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }


    private void saveRefreshToken(AccountEntity account, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setAccount(account);
        refreshTokenEntity.setRevoked(false);
        refreshTokenDAO.save(refreshTokenEntity);
    }

    private void updateRevokedRefreshToken(AccountEntity account) {
        List<RefreshTokenEntity> listRefreshTokenAccount = refreshTokenDAO.findByAccountAndRevoked(account, false);
        listRefreshTokenAccount.forEach(it -> it.setRevoked(true));
        refreshTokenDAO.saveAll(listRefreshTokenAccount);
    }

}
