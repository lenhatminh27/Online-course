package com.course.service.impl;

import com.course.common.utils.MessageUtils;
import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.dao.AccountDAO;
import com.course.dao.RefreshTokenDAO;
import com.course.dto.request.AuthenticationRequest;
import com.course.dto.request.LogoutRequest;
import com.course.dto.response.AuthenticationResponse;
import com.course.entity.AccountEntity;
import com.course.entity.RefreshTokenEntity;
import com.course.exceptions.AuthenticationException;
import com.course.security.TokenProvider;
import com.course.security.context.AuthenticationContext;
import com.course.service.AuthenticationService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

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
        return authenticationResponse(account);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        String refreshTokenInReq = tokenProvider.resolveToken(req);
        RefreshTokenEntity refreshTokenEntity = refreshTokenDAO.findByRefreshTokenAnsRevoked(refreshTokenInReq, false);
        if(ObjectUtils.isEmpty(refreshTokenInReq)) {
            ResponseUtils.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, gson.toJson("Internal server error"));
        }
        AccountEntity account = refreshTokenEntity.getAccount();
        return authenticationResponse(account);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        String refreshTokenInReq = logoutRequest.getRefreshToken();
        RefreshTokenEntity refreshTokenEntity = refreshTokenDAO.findByRefreshTokenAnsRevoked(refreshTokenInReq, false);
        if(ObjectUtils.isEmpty(refreshTokenEntity)) {
            throw new AuthenticationException("Bad request");
        }
        refreshTokenEntity.setRevoked(true);
        refreshTokenDAO.update(refreshTokenEntity);
    }

    private AuthenticationResponse authenticationResponse(AccountEntity account) {
        AuthenticationContext context = new AuthenticationContext();
        context.setEmail(account.getEmail());
        List<String> authorities = account
                .getRoles()
                .stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(role.getName().name()),
                        role.getPermissions().stream().map(permission -> permission.getName().name())
                ))
                .distinct()
                .toList();
        context.setAuthorities(authorities);
        String accessToken = tokenProvider.createAccessToken(context);
        String refreshToken = tokenProvider.createRefreshToken(context, false);
        //revoked refresh token
        updateRevokedRefreshToken(account);

        //save refresh token
        saveRefreshToken(account, refreshToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void updateRevokedRefreshToken(AccountEntity account) {
        List<RefreshTokenEntity> listRefreshTokenAccount = refreshTokenDAO.findByAccountAndRevoked(account, false);
        listRefreshTokenAccount.forEach(it -> it.setRevoked(true));
        refreshTokenDAO.saveAll(listRefreshTokenAccount);
    }

    private void saveRefreshToken(AccountEntity account, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setAccount(account);
        refreshTokenEntity.setRevoked(false);
        refreshTokenDAO.save(refreshTokenEntity);
    }

}
