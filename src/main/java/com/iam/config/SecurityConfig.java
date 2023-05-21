package com.iam.config;

import com.iam.auth.ApiKeyAuthFilter;
import com.iam.auth.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint entryPoint;
    private final AppConfig appConfig;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain apiKeyFilterChain(HttpSecurity httpSecurity, AuthenticationManager authManager) throws Exception {

        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(authManager, appConfig);
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessManagement -> sessManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .addFilter(apiKeyAuthFilter)
                .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(entryPoint))
                .build();
    }
}
