package com.pickple.commerceservice.infrastructure.configuration;

import jakarta.annotation.Nonnull;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

//    @Override
//    public @Nonnull Optional<String> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//        return Optional.of(authentication.getName());
//    }

    // test
    @Override
    public @Nonnull Optional<String> getCurrentAuditor() {
        return Optional.of("defaultUser");
    }

}