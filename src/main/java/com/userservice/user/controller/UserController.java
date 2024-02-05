package com.userservice.user.controller;

import com.userservice.global.dto.WebResponseData;
import com.userservice.user.domain.dto.*;
import com.userservice.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public UserDto userInfo(Authentication auth) {

        return userService.getUserInfo(auth);
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

    @GetMapping("/getUserName")
    public String getUserName(@RequestParam Long userId) {
        return userService.getUserName(userId);
    }

    @GetMapping("/infoForFollow")
    public UserFollowDto getInfoForFollow(Authentication auth, @RequestParam Long followId) {
        return userService.getInfoForFollow(auth, followId);
    }

}
