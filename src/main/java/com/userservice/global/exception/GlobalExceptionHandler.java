package com.userservice.global.exception;

import com.userservice.global.dto.WebResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public WebResponseData<Object> userExceptionHandler(UserException userException){
        return WebResponseData.error(userException.getErrorCode());
    }
}
