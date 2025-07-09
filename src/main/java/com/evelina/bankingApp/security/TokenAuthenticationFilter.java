package com.evelina.bankingApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final SimpleTokenManager tokenManager;

    @Autowired
    public TokenAuthenticationFilter(SimpleTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        logger.info(">>> TokenAuthenticationFilter started for request: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn(">>> No 'Bearer ' token found in Authorization header. Passing request down the chain.");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        logger.info(">>> Extracted token: {}", token);

        tokenManager.getUsernameByToken(token).ifPresentOrElse(
                username -> {
                    // if token is found
                    logger.info(">>> Token is valid. Authenticating user: {}", username);

                    UserDetails userDetails = new User(
                            username,
                            "",
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info(">>> SecurityContextHolder updated for user: {}", username);
                },
                () -> {
                    // if token is not found
                    logger.warn(">>> Token is invalid or expired. Clearing SecurityContext.");
                    SecurityContextHolder.clearContext();
                }
        );

        filterChain.doFilter(request, response);
        logger.info(">>> TokenAuthenticationFilter finished for request: {}", request.getRequestURI());
    }
}
