package com.pickple.auth.application.service;

import com.pickple.auth.application.domain.model.User;
import com.pickple.auth.application.dto.UserDto;
import com.pickple.auth.application.security.JwtUtil;
import com.pickple.auth.application.security.UserDetailsImpl;
import com.pickple.auth.infrastructure.feign.UserServiceClient;
import com.pickple.auth.presentation.request.LoginRequestDto;
import com.pickple.auth.presentation.request.SignUpRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final RedisService redisService;

    // 로그인
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        Collection<GrantedAuthority> roles = ((UserDetailsImpl) authentication.getPrincipal()).getAuthorities();
        List<String> rolesList = roles.stream().map(GrantedAuthority::getAuthority).toList();
        String rolesString = rolesList.toString();

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        UserDto userDto = UserDto.convertToUserDto(user);
        redisService.setValue("user:" + username, userDto);

        String jwt = jwtUtil.createToken(username, roles);
        jwtUtil.addJwtToHeader(jwt, response);

        response.addHeader("X-User-Name", username);
        response.addHeader("X-User-Roles", rolesString);
    }

    // 회원 가입
    public Boolean signup(SignUpRequestDto signUpDto) {
        String password = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(password);
        return userServiceClient.registerUser(signUpDto);
    }
}
