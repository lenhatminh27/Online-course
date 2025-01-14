package com.course.config.filter;

import com.course.common.utils.StringUtils;
import com.course.security.TokenProvider;
import com.course.security.context.AuthenticationContext;
import com.course.security.context.AuthenticationContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class AccessTokenFilter implements Filter {

    private final TokenProvider tokenProvider;

    public AccessTokenFilter() {
        this.tokenProvider = new TokenProvider();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initializing AccessTokenFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // Resolve token from request header
        String token = tokenProvider.resolveToken(httpRequest);
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // Parse the token to extract email and authorities
            var claims = tokenProvider.getJwtParser().parseClaimsJws(token).getBody();
            String email = claims.getSubject();
            String authorities = claims.get(TokenProvider.AUTHORITIES_KEY, String.class);
            // Create AuthenticationContext and populate thread-local storage
            AuthenticationContext authenticationContext = new AuthenticationContext();
            authenticationContext.setEmail(email);
            authenticationContext.setAuthorities(List.of(authorities.split(",")));
            AuthenticationContextHolder.setContext(authenticationContext);
            log.debug("Authenticated user '{}', setting security context", email);
        } else {
            log.debug("No valid JWT token found for request {}", httpRequest.getRequestURI());
        }
        try {
            // Continue the filter chain
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // Clear context after processing
            AuthenticationContextHolder.clearContext();
        }
    }

    @Override
    public void destroy() {
        log.info("Destroying AccessTokenFilter");
    }
}
