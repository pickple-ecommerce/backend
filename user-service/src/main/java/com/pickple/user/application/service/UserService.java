package com.pickple.user.application.service;

import com.pickple.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Boolean registerUser(SignUpRequestDto signUpDto) {
        User user = User.convertSignUpDtoToUser(signUpDto);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("회원 가입에서 User 저장 시 오류 발생");
            return false;
        }
    }
}
