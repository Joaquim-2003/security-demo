package com.jing.security.exception;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> exceptionHandler(BizException e) {
        return Result.error(e.getResultCode());
    }

    /**
     * 参数校验异常
     *
     * @param e 参数校验异常
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> validationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));

        return Result.error(ResultCode.PARAM_ERROR, message.toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(ResultCode.FORBIDDEN);
    }

    /**
     * 系统异常处理
     *
     * @param e 系统异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handlerSystemException(Exception e) {
        log.error("系统异常", e);

        return Result.error(ResultCode.SYSTEM_ERROR);
    }
}
