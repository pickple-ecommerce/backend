package com.pickple.user.presentation.controller;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.user.application.dto.UserDto;
import com.pickple.user.application.dto.UserResponseDto;
import com.pickple.user.application.service.UserService;
import com.pickple.user.presentation.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원 전체 조회
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "회원 전체 조회 성공", users));
    }

    @PostMapping("/sign-up")
    Boolean registerUser(@RequestBody SignUpRequestDto signUpDto) {
        return userService.registerUser(signUpDto);
    }

    @GetMapping("/user/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

}
