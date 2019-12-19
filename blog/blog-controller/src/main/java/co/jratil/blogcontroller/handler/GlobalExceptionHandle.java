package co.jratil.blogcontroller.handler;

import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.response.ResponseUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(GlobalException.class)
    public ResponseVO handleException(GlobalException e) {
        return ResponseUtils.error(e.getCode(), e.getMessage());
    }
}
