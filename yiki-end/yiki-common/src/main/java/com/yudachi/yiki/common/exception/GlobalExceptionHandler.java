package com.yudachi.yiki.common.exception;

import com.yudachi.yiki.common.response.YikiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 定义要捕获的异常 可以多个 @ExceptionHandler({})
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(CustomizeInfoException.class)
    public YikiResponse customExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        CustomizeInfoException exception = (CustomizeInfoException) e;
        return YikiResponse.createResponse(exception.getCode(), exception.getMessage());
    }
 
    /**
     * 捕获  RuntimeException 异常
     * TODO  如果你觉得在一个 exceptionHandler 通过  if (e instanceof xxxException) 太麻烦
     * TODO  那么你还可以自己写多个不同的 exceptionHandler 处理不同异常
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public YikiResponse runtimeExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        return YikiResponse.createResponse(400, exception.getMessage());
    }
 }