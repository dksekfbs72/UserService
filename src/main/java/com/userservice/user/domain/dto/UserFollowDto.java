package com.userservice.user.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserFollowDto {
    private Long id;
    private String name;
    private String email;
    private String followUserName;
}
