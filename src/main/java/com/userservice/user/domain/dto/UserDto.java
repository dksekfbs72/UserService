package com.userservice.user.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
