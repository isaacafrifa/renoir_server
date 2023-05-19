package com.iam.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain apiKeyFilterChain(HttpSecurity httpSecurity) throws Exception {

       return httpSecurity
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(sessManagement -> sessManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth ->
                       auth.anyRequest().authenticated())
               .httpBasic(Customizer.withDefaults()) //TODO change to apiKey auth
               .build();
    }

}
