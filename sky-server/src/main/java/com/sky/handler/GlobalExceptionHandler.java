package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<?> exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    // RequestBody参数异常
    // 触发场景：当使用@Valid注解对@RequestBody参数进行验证时，如果请求体不符合约束条件（如字段为空、长度不符合要求等），会触发此异常。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorObjectName = ex.getObjectName();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        Objects.requireNonNull(fieldError);
        String errorField = fieldError.getField();
        log.error("异常信息：{}", errorObjectName + "中" + errorField + "传值有误");
        return Result.error(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    // RequestParams或者PathVariable参数异常
    // 在Controller中使用@Validated检验RequestParams或者PathVariable参数，如果不符合约束条件，会触发此异常
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleValidationExceptions(ConstraintViolationException e) {
        log.error("异常信息: {}", e.getLocalizedMessage());
        return Result.error(e.getMessage().split(":")[1]);
    }

    // 当使用@ModelAttribute绑定查询参数或表单数据到对象时，如果绑定失败（如类型转换错误或字段不符合约束条件），会触发此异常
    // 当不使用@RequestParams、@PathVariable、@RequestBody时，默认使用@ModelAttribute
    @ExceptionHandler(BindException.class)
    public Result<String> handleValidationExceptions(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        Objects.requireNonNull(fieldError);
        // 获取字段名称
        String fieldName = fieldError.getField();
        // 获取错误代码（用于区分类型转换失败和校验失败）
        String errorCode = fieldError.getCode();
        // 根据错误代码返回不同的错误信息
        if ("typeMismatch".equals(errorCode)) {
            // 类型转换失败
            String requireTypeName = Objects.requireNonNull(e.getFieldType(fieldName)).getSimpleName();
            String errorMessage = String.format("字段 '%s' 期望的类型为 %s", fieldName, requireTypeName);
            log.error("异常信息：类型转换异常：{}", errorMessage);
            return Result.error(errorMessage);
        } else {
            // 校验失败
            String validationMessage = fieldError.getDefaultMessage();
            String errorMessage = String.format("字段 '%s' 校验失败：%s", fieldName, validationMessage);
            log.error("异常信息：校验异常：{}", errorMessage);
            return Result.error(errorMessage);
        }
    }

    // RequestParams传递空值时异常
    // 当请求中缺少必需的查询参数（@RequestParam）或路径参数（@PathVariable）时触发
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> exceptionHandler(MissingServletRequestParameterException exception) {
        String name = exception.getParameterName();
        log.error("异常信息：{}", name + "参数传递为空");
        return Result.error(name + "参数传递为空");
    }

    // RequestParams或者PathVariable参数不匹配时异常
    // 当查询参数或路径参数的类型与Controller方法中声明的参数类型不匹配时触发
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<String> exceptionHandler(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        String requiredTypeName = Objects.requireNonNull(exception.getRequiredType()).getSimpleName();
        log.error("异常信息：{}", name + "类型不匹配, 期望类型为：" + requiredTypeName);
        return Result.error(name + "类型不匹配, 期望类型为：" + requiredTypeName);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> exceptionHandler(HttpRequestMethodNotSupportedException exception) {
        log.error("异常信息：{}", exception.getMessage());
        return Result.error("错误的请求方式");
    }

    @ExceptionHandler({Exception.class})
    public Result<?> exceptionHandler(Exception e) {
        log.error("异常信息：", e);
        return Result.error("未知异常");
    }

}
