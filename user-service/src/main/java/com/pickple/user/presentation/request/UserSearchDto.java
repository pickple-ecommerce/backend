package com.pickple.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private String username;
    private String nickname;
    private String email;
    private String role;
}
