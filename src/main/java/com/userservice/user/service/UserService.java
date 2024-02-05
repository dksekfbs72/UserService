package com.userservice.user.service;

import com.userservice.global.config.jwt.JwtTokenUtil;
import com.userservice.global.email.MailComponents;
import com.userservice.global.exception.UserException;
import com.userservice.global.type.ErrorCode;
import com.userservice.user.domain.dto.*;
import com.userservice.user.domain.entity.User;
import com.userservice.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.System.getenv;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final MailComponents mailComponents;
    private final RedisTemplate<String, Object> redisTemplate;

    public String signUp(SingUpForm req) {
        // loginId 중복 체크
        if (checkLoginIdDuplicate(req.getEmail())) {
            throw new UserException(ErrorCode.REGISTERED_EMAIL);
        }
        // 닉네임 중복 체크
        if (checkNicknameDuplicate(req.getName())) {
            throw new UserException(ErrorCode.REGISTERED_NICKNAME);
        }
        // password와 passwordCheck가 같은지 체크
        if (!req.getPassword().equals(req.getPasswordCheck())) {
            throw new UserException(ErrorCode.PASSWORD_CHECK_EXCEPTION);
        }
        String uuid = UUID.randomUUID().toString();
        mailComponents.sendMail(req.getEmail(),
                "[예약 구매 서비스] 회원 가입을 축하드립니다.",
                    "<p>안녕하세요. 예약 구매 서비스 회원 가입을 축하드립니다.<p>" +
                        "<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.<p>" +
                        "<a href='http://localhost:8080/user/emailAuth?emailKey=" + uuid + "'>회원가입 완료</a>");
        userRepository.save(req.toEntity(encoder.encode(req.getPassword()), uuid));
        return "회원가입 성공";
    }


    public String login(LoginForm req) {
        Optional<User> optionalUser = userRepository.findByEmail(req.getEmail());

        // 유저를 찾을 수 없음
        if (optionalUser.isEmpty()) {
            throw new UserException(ErrorCode.NOT_FOUND_EMAIL);
        }

        User user = optionalUser.get();

        // 비밀번호 오류
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.WRONG_PASSWORD);
        }
        // 이메일 인증 오류
        if (!user.isEmailCert()){
            throw new UserException(ErrorCode.NOT_HAVE_EMAIL_AUTH);
        }

        return JwtTokenUtil.createToken(user.getEmail());
    }

    public String logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        long expiration = JwtTokenUtil.getExpiration(token, getenv().get("SECRET_KEY")).getTime();
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.SECONDS);
        return "로그아웃 성공";
    }

    public String emailAuth(String emailKey) {
        Optional<User> optionalUser = userRepository.findByEmailKey(emailKey);
        if (optionalUser.isEmpty()) {
            throw new UserException(ErrorCode.NOT_FOUND_USER);
        }
        User user = optionalUser.get();
        user.setEmailCert(true);
        userRepository.save(user);
        return "이메일 인증 성공";
    }

    public String updateInfo(UpdateInfoForm updateInfoForm, Authentication auth) {
        User user = whoIAm(auth);

        String newName = updateInfoForm.getName();
        String newProfileImage = updateInfoForm.getProfileImage();
        String newDescription = updateInfoForm.getDescription();
        System.out.println(newName +" "+ newProfileImage + " "+ newDescription);
        if (newName != null) user.setName(newName);
        if (newProfileImage != null) user.setProfileImage(newProfileImage);
        if (newDescription != null) user.setDescription(newDescription);

        userRepository.save(user);
        return "내 정보 수정 성공";
    }

    public String updatePassword(UpdatePasswordForm updatePasswordForm, Authentication auth) {
        User user = whoIAm(auth);

        if (!encoder.matches(updatePasswordForm.getPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.WRONG_PASSWORD);
        }

        user.setPassword(encoder.encode(updatePasswordForm.getNewPassword()));

        userRepository.save(user);

        return "비밀번호 수정 성공";
    }
    public User whoIAm(Authentication auth) {
        Optional<User> optionalUser = userRepository.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            throw new UserException(ErrorCode.NOT_FOUND_USER);
        }
        return optionalUser.get();
    }

    public User getLoginUserByLoginId(String email) {
        if (email == null) return null;

        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public boolean checkLoginIdDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplicate(String name) {
        return userRepository.existsByName(name);
    }

    public UserDto getUserInfo(Authentication auth) {
        User loginUser = getLoginUserByLoginId(auth.getName());
        return UserDto.builder()
                .id(loginUser.getId())
                .name(loginUser.getName())
                .email(loginUser.getEmail())
                .build();
    }

    public String getUserName(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserException(ErrorCode.NOT_FOUND_USER);
        }
        return optionalUser.get().getName();
    }

    public UserFollowDto getInfoForFollow(Authentication auth, Long followId) {
        User user = whoIAm(auth);
        Optional<User> follow = userRepository.findById(followId);
        if (follow.isEmpty()){
            throw new UserException(ErrorCode.NOT_FOUND_USER);
        }
        return UserFollowDto.builder()
                .followUserName(follow.get().getName())
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
