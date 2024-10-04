package com.pickple.user.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.user.application.dto.UserDto;
import com.pickple.user.application.dto.UserResponseDto;
import com.pickple.user.domain.model.User;
import com.pickple.user.domain.repository.UserRepository;
import com.pickple.user.exception.UserErrorCode;
import com.pickple.user.presentation.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 전체 조회
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllByIsDeleteFalse().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    // 회원 상세 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUsername(String username) {
        User user = findUserByUsername(username);
        return UserResponseDto.from(user);
    }

    // 유저 존재 여부 확인 메서드
    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return UserDto.convertToUserDto(user);
    }

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
