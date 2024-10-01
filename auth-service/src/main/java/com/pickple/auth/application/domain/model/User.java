package com.pickple.auth.application.domain.model;

import com.pickple.auth.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String username;
    private String password;
    private List<String> roles;

    public static User convertToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(new ArrayList<>(userDto.getRoles()))
                .build();
    }
}
