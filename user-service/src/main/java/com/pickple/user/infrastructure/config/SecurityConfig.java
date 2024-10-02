package com.pickple.user.infrastructure.config;

import com.pickple.user.application.security.CustomPreAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomPreAuthFilter customPreAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF disable
        http.csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 인증이 필요 없는 경로 설정
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/v1/users/sign-up").permitAll()  // 회원 가입 경로는 인증 없이 허용
                        .requestMatchers("/api/v1/users/user/{username}").permitAll()
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
        );

        // 필터 관리
        http.addFilterBefore(customPreAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
