package com.userservice.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    LOGIN_FAIL("로그인 실패"),
    SUCCESS_CODE("성공"),
    REGISTERED_EMAIL("이미 등록된 이메일입니다."),
    REGISTERED_NICKNAME("이미 등록된 닉네임입니다."),
    WRONG_PASSWORD("잘못된 비밀번호입니다."),
    PASSWORD_CHECK_EXCEPTION("비밀번호 확인과 비밀번호가 다릅니다."),
    NOT_FOUND_EMAIL("잘못된 이메일입니다."),
    NOT_HAVE_EMAIL_AUTH("이메일 인증이 필요합니다."),
    NOT_FOUND_USER("회원을 찾을 수 없습니다."),
    ALREADY_FOLLOW_USER("이미 팔로우한 회원입니다."),
    NOT_FOUND_POST("포스트를 찾을 수 없습니다.");

    private final String description;
}
