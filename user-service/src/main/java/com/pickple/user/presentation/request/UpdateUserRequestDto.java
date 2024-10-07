package com.pickple.user.presentation.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {

    @Size(min = 4, max = 10)
    private String nickname;
    private String password;

}
