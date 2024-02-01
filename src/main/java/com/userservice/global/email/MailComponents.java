package com.userservice.global.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailComponents {
    private final JavaMailSender javaMailSender;

    public void sendMail(String userId, String subject, String text) {
        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(userId);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        };
        javaMailSender.send(msg);
    }
}
