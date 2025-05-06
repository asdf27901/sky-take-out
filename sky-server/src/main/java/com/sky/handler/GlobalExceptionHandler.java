package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<?> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    // RequestBody参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorObjectName = ex.getObjectName();
        String errorField = ex.getFieldError().getField();
        log.error("异常信息：{}", errorObjectName + "中" + errorField + "传值有误");
        return Result.error(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    // RequestParams或者PathVariable参数异常
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleValidationExceptions(ConstraintViolationException e){
        log.error("异常信息: {}", e.getLocalizedMessage());
        return Result.error(e.getMessage().split(":")[1]);
    }

    // RequestParams传递空值时异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> exceptionHandler(MissingServletRequestParameterException exception){
        String name = exception.getParameterName();
        log.error("异常信息：{}", name + "参数传递为空");
        return Result.error(name + "不能传递空值");
    }

    // RequestParams或者PathVariable参数不匹配时异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<String> exceptionHandler(MethodArgumentTypeMismatchException exception){
        String name = exception.getName();
        Class<?> requiredType = exception.getRequiredType();
        log.error("异常信息：{}", name + "类型不匹配, 期望类型为：" + requiredType);
        return Result.error(name + "类型不匹配, 期望类型为：" + requiredType);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> exceptionHandler(HttpRequestMethodNotSupportedException exception){
        log.error("异常信息：{}", exception.getMessage());
        return Result.error("错误的请求方式");
    }

    @ExceptionHandler({Exception.class})
    public Result<?> exceptionHandler(Exception e){
        log.error("异常信息：{}", e.getMessage());
        e.printStackTrace();
        return Result.error("未知异常");
    }

}
