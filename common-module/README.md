# common_module

Pickple Backend 에서 공통으로 사용되는 클래스, 로직들을 포함하는 레포지토리 입니다.

# Common Module 사용법

## 1. `common-module` 모듈 의존성 추가

`common-module`을 사용하려면, 먼저 **Gradle** 설정 파일에 `common-module`을 의존성으로 추가해야 합니다.

### `build.gradle` 설정

모듈을 사용할 프로젝트의 `build.gradle` 파일에 아래 코드를 추가합니다.

```gradle
dependencies {
    implementation project(':common-module')  // common-module을 의존성으로 추가
}
```

### `settings.gradle` 설정

`settings.gradle` 파일에 `common-module`을 프로젝트에 포함시키고, 경로를 지정합니다. 아래 코드를 `settings.gradle`에 추가합니다.

```gradle
// common-module을 프로젝트에 포함시킵니다.
include ':common-module'

// common-module의 경로를 절대 경로로 지정합니다.
project(':common-module').projectDir = new File('../common-module')
```

`projectDir` 경로는 실제 환경에 맞게 수정해 주세요.

## 2. `BaseEntity` 사용을 위한 설정

`common-module`에서 제공하는 `BaseEntity`를 사용하려면, 각 서비스에서 JPA Auditing 기능을 활성화해야 합니다. 이를 위해 `JpaConfig` 클래스와 `AuditAwareImpl` 클래스를 구현하십시오.

### `JpaConfig.java` 설정

```java
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
```

### `AuditAwareImpl.java` 설정

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.AuditorAware;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public @Nonnull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(authentication.getName());
    }
}
```

이 설정을 통해 각 엔티티에서 생성 및 수정한 사용자의 정보를 추적할 수 있습니다.