package com.userservice.user.domain.dto;

import com.userservice.global.type.UserRole;
import com.userservice.user.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingUpForm {
    @NotBlank(message = "이메일을 설정해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 설정해주세요.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "이름을 설정해주세요.")
    private String name;
    @NotBlank(message = "프로필 이미지를 설정해주세요.")
    private String profileImage;
    @NotBlank(message = "인삿말을 설정해주세요.")
    private String description;

    // 비밀번호 암호화
    public User toEntity(String encodedPassword, String emailKey) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .profileImage(this.profileImage)
                .description(this.description)
                .role(UserRole.USER)
                .emailCert(false)
                .emailKey(emailKey)
                .build();
    }
}
