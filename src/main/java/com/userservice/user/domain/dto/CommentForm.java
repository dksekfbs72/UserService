package com.userservice.user.domain.dto;

import com.userservice.user.domain.entity.Comment;
import com.userservice.user.domain.entity.Post;
import com.userservice.user.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentForm {
    private String description;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .post(post)
                .user(user)
                .text(this.description)
                .build();
    }
}
