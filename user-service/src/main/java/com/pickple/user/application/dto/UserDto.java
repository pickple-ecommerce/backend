package com.pickple.user.application.dto;

import com.pickple.user.domain.model.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;
    private String password;
    private List<String> roles;

    public static UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().getAuthority());
        userDto.setRoles(roles);
        return userDto;
    }
}
