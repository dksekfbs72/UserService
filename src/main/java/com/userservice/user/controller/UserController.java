package com.userservice.user.controller;

import com.userservice.global.dto.WebResponseData;
import com.userservice.user.domain.dto.*;
import com.userservice.user.domain.entity.User;
import com.userservice.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public WebResponseData<String> join(@RequestBody SingUpForm singUpForm) {

        return WebResponseData.ok(userService.signUp(singUpForm));
    }

    @PostMapping("/login")
    public WebResponseData<String> login(@RequestBody LoginForm loginForm) {

        return WebResponseData.ok(userService.login(loginForm));
    }

    @GetMapping("/emailAuth")
    public WebResponseData<String> emailAuth(@RequestParam String emailKey){
        return WebResponseData.ok(userService.emailAuth(emailKey));
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId : %s\nnickname : %s\nrole : %s",
                loginUser.getEmail(), loginUser.getName(), loginUser.getRole().name());
    }

    @DeleteMapping("/logout")
    public WebResponseData<String> logout(HttpServletRequest request) {
        return WebResponseData.ok(userService.logout(request));
    }

    @PutMapping
    public WebResponseData<String> updateInfo(@RequestBody UpdateInfoForm updateInfoForm, Authentication auth) {
        return WebResponseData.ok(userService.updateInfo(updateInfoForm, auth));
    }

    @PutMapping("/updatePassword")
    public WebResponseData<String> updatePassword(@RequestBody UpdatePasswordForm updatePasswordForm, Authentication auth, HttpServletRequest request) {
        userService.logout(request);
        return WebResponseData.ok(userService.updatePassword(updatePasswordForm, auth));
    }

    @GetMapping("/feed")
    public WebResponseData<Page<FeedDto>> getMyFeed(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return WebResponseData.ok(userService.getMyFeed(auth, page, size));
    }

    @PostMapping("/post")
    public WebResponseData<String> writePost(
            Authentication auth,
            @RequestBody PostForm postForm
    ) {
      return WebResponseData.ok(userService.writePost(auth, postForm));
    }

    @PostMapping("/likePost")
    public WebResponseData<String> likePost(
            Authentication auth,
            @RequestParam long postId
    ) {
        return WebResponseData.ok(userService.likePost(auth, postId));
    }
    @PostMapping("/likeComment")
    public WebResponseData<String> likeComment(
            Authentication auth,
            @RequestParam long commentId
    ) {
        return WebResponseData.ok(userService.likeComment(auth, commentId));
    }

    @PostMapping("/comment")
    public WebResponseData<String> writeComment(
            Authentication auth,
            @RequestParam long postId,
            @RequestBody CommentForm commentForm
    ) {
        return WebResponseData.ok(userService.writeComment(auth, postId, commentForm));
    }
}
