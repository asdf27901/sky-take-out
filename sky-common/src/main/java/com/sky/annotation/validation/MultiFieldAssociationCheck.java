package com.sky.annotation.validation;

import com.sky.validator.MultiFieldAssociationCheckValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultiFieldAssociationCheckValidator.class) // 需要指定校验器对象
@Repeatable(MultiFieldAssociationCheck.List.class) // 指定存放重复注解的容器
public @interface MultiFieldAssociationCheck {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        MultiFieldAssociationCheck[] value() default {};
    }

    /**
     * 当什么条件下校验,必须是一个spel表达式
     */
    String when();

    /**
     * 必须满足什么条件,必须是一个spel表达式
     */
    String must();

    /**
     * validation 必须要指定的默认异常信息
     */
    String message();

    /**
     * 错误属性名
     */
    String errorField();

    /**
     * validation 执行所属分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载参数
     */
    Class<? extends Payload>[] payload() default {};
}
