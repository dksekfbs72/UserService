package com.userservice.user.controller;

import com.userservice.global.dto.WebResponseData;
import com.userservice.user.domain.dto.LoginForm;
import com.userservice.user.domain.dto.SingUpForm;
import com.userservice.user.domain.dto.UpdateInfoForm;
import com.userservice.user.domain.dto.UpdatePasswordForm;
import com.userservice.user.domain.entity.User;
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
}
