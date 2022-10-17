package com.iam.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iam.exception.APIError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class ApiKeyAuthenticationFilter implements Filter {

    @Value("${auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${auth-token}")
    private String principalRequestValue;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String apiKey = getApiKey((HttpServletRequest) request);
            if (apiKey.equals(principalRequestValue)) {
                ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(apiToken);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                log.error("Authentication Failed. IP Address [{}]", request.getRemoteAddr());

                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpResponse.setContentType("application/json");
                APIError errorDetails = new APIError(HttpStatus.UNAUTHORIZED.value(),
                        "Authentication Failed",
                        ((HttpServletRequest) request).getRequestURI(),
                        LocalDateTime.now());
                // httpResponse.getWriter().write("Invalid API Key");

                /* By default, ObjectMapper doesn't understand the LocalDateTime class,
                 so I had to add jackson dependency in my maven */
                objectMapper.findAndRegisterModules();  // add LOC to register objectMapper so it works with LocalDateTime.
                httpResponse.getWriter()
                            .write(objectMapper.writeValueAsString(errorDetails));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getApiKey(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest.getHeader(principalRequestHeader))
                .orElseGet(() -> "YAWA") // return yawa instead of null
                .toLowerCase().trim();
    }
}

