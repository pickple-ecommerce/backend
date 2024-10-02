package com.pickple.commerceservice.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@Configuration
@EnableJpaAuditing  // JPA Auditing 활성화
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditAwareImpl();
    }

}