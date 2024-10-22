package com.pickple.gateway.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserDto implements Serializable {
    private String username;
    private String email;
    private List<String> roles;
}
