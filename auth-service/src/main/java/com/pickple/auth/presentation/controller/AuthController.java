package com.pickple.auth.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDto signUpDto) {
        Boolean result = authService.signup(signUpDto);
        if (result) {
            return ResponseEntity.ok().body("signup success");
        } else {
            return ResponseEntity.badRequest().body("signup failed");
        }
    }

}
