package com.pickple.user.presentation.controller;

import com.pickple.user.application.dto.UserDto;
import com.pickple.user.application.service.UserService;
import com.pickple.user.presentation.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    Boolean registerUser(@RequestBody SignUpRequestDto signUpDto) {
        return userService.registerUser(signUpDto);
    }

    @GetMapping("/user/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

}
