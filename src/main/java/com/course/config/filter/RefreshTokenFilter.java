package com.course.config.filter;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.ResponseUtils;
import com.course.common.utils.StringUtils;
import com.course.dao.RefreshTokenDAO;
import com.course.dao.impl.RefreshTokenDaoImpl;
import com.course.entity.RefreshTokenEntity;
import com.course.security.TokenProvider;
import com.course.security.context.AuthenticationContext;
import com.course.security.context.AuthenticationContextHolder;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class RefreshTokenFilter implements Filter {

    private final TokenProvider tokenProvider;

    public RefreshTokenFilter() {
        this.tokenProvider = new TokenProvider();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("[JwtRefreshTokenFilter:doFilterInternal] :: Started ");

        log.info("[JwtRefreshTokenFilter:doFilterInternal]Filtering the Http Request:{}", httpRequest.getRequestURI());

        // Logic xác thực refresh token
        final var refreshToken = tokenProvider.resolveToken(httpRequest);
        if (ObjectUtils.isEmpty(refreshToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            var claims = tokenProvider.getJwtParser().parseClaimsJws(refreshToken).getBody();
            String email = claims.getSubject();
            if (StringUtils.hasText(email)) {
                boolean isRefreshTokenValidInDatabase = isRefreshTokenValidInDatabase(refreshToken);
                if (isRefreshTokenValidInDatabase && tokenProvider.validateToken(refreshToken)) {
                    String authorities = claims.get(TokenProvider.AUTHORITIES_KEY, String.class);
                    // Create AuthenticationContext and populate thread-local storage
                    AuthenticationContext authenticationContext = new AuthenticationContext();
                    authenticationContext.setEmail(email);
                    authenticationContext.setAuthorities(List.of(authorities.split(",")));
                    AuthenticationContextHolder.setContext(authenticationContext);
                    log.debug("Authenticated user '{}', setting security context", email);
                }
            }else {
                log.debug("No valid JWT token found for request {}", httpRequest.getRequestURI());
            }
        }catch (Exception e){
            Gson gson = new Gson();
            ResponseUtils.writeResponse((HttpServletResponse)response, HttpServletResponse.SC_UNAUTHORIZED, gson.toJson("Unauthorized"));
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthenticationContextHolder.clearContext();
        }
    }

    private boolean isRefreshTokenValidInDatabase(String refreshToken) {
        RefreshTokenDAO refreshTokenDAO = new RefreshTokenDaoImpl();
        RefreshTokenEntity byRefreshTokenAnsRevoked = refreshTokenDAO.findByRefreshTokenAnsRevoked(refreshToken, false);
        return !ObjectUtils.isEmpty(byRefreshTokenAnsRevoked);
    }

}
