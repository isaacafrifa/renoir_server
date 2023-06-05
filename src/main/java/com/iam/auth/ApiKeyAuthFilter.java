package com.iam.auth;

import com.iam.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Slf4j
public class ApiKeyAuthFilter extends BasicAuthenticationFilter {
    private final AppConfig appConfig;

    public ApiKeyAuthFilter(AuthenticationManager authenticationManager, AppConfig appConfig) {
        super(authenticationManager);

        this.appConfig = appConfig;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String apiKey = extractApiKey(request);

        if (appConfig.getAuthToken().toLowerCase().equals(apiKey)) {
            ApiKeyAuthenticationToken authentication = new ApiKeyAuthenticationToken(apiKey, List.of(new SimpleGrantedAuthority("ROLE_API")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication Successful");
        }
        chain.doFilter(request, response);
    }

    private String extractApiKey(HttpServletRequest request) {
        // Return null if the API key is not found
        return Objects.requireNonNull(request.getHeader(appConfig.getAuthHeaderName()))
                .toLowerCase().trim();
    }

}

