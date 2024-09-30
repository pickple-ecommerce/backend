package com.pickple.delivery.infrastructure.config;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public @Nonnull Optional<String> getCurrentAuditor() {
        // TODO: 요청한 User의 Id로 설정
        return Optional.of("system_user");
    }
}