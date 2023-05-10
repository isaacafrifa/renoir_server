package com.iam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/* This class was created just to prevent
 placing @EnableJpaAuditing in the main Spring class */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
