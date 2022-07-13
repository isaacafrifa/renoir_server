package com.iam.config;


import com.iam.auth.APIKeyAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //this LOC enables the @PreAuthorize annotation.
@Order(1)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${auth-token}")
    private String principalRequestValue;

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                if (!principalRequestValue.equals(principal)) {
                    log.error("API Key[{}] was not found or not the expected value", principal);
                    throw new BadCredentialsException("Bad credentials");
//                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"you can't consume this service , Please validate your apikeys");

//                    HttpServletResponse httpResponse = (HttpServletResponse) response;
//                    httpResponse.setStatus(401);
//                    httpResponse.getWriter().write("Invalid API Key");
                }
//                ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
//                SecurityContextHolder.getContext().setAuthentication(apiToken);
                authentication.setAuthenticated(true);
                return authentication;
            }
        });

        httpSecurity.
                antMatcher("/api/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .authorizeRequests()
                .anyRequest().authenticated();

    }
}
