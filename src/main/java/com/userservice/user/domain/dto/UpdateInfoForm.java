package com.userservice.user.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateInfoForm {
    private String name;
    private String profileImage;
    private String description;
}
