package com.pickple.user.domain.model;

import com.pickple.common_module.domain.model.BaseEntity;
import com.pickple.user.presentation.request.SignUpRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "p_users")
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Size(min = 4, max = 10)  // 길이를 4~10자로 제한
    @Pattern(regexp = "^[a-z0-9]+$")  // 알파벳 소문자와 숫자로만 구성
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Size(min = 4, max = 10)
    @Column(nullable = false, unique = true)
    private String nickname;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public void updateUserInfo(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void markAsDeleted() {
        this.isDelete = true;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public static User convertSignUpDtoToUser(SignUpRequestDto signUpDto) {
        return User.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .email(signUpDto.getEmail())
                .role(UserRole.fromString(signUpDto.getRole()))
                .build();
    }

}
