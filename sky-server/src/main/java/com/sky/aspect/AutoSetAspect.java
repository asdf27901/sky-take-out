package com.sky.aspect;

import com.sky.annotation.AutoSet;
import com.sky.constant.AutoSetConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
public class AutoSetAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoSet)")
    private void autoSetPointcut() {
    }

    @SneakyThrows
    @Before("autoSetPointcut()")
    public void autoSet(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoSet autoSet = signature.getMethod().getAnnotation(AutoSet.class);
        OperationType operationType = autoSet.value();

        Object[] args = joinPoint.getArgs();
        if (args.length != 0) {
            Object o = args[0];

            LocalDateTime now = LocalDateTime.now();
            Long empId = BaseContext.getCurrentId();

            Method setCreateTimeMethod = o.getClass().getDeclaredMethod(AutoSetConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTimeMethod = o.getClass().getDeclaredMethod(AutoSetConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateUserMethod = o.getClass().getDeclaredMethod(AutoSetConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUserMethod = o.getClass().getDeclaredMethod(AutoSetConstant.SET_UPDATE_USER, Long.class);

            if (Objects.requireNonNull(operationType) == OperationType.INSERT) {
                setCreateTimeMethod.invoke(o, now);
                setCreateUserMethod.invoke(o, empId);
            }
            setUpdateTimeMethod.invoke(o, now);
            setUpdateUserMethod.invoke(o, empId);
        }

    }
}
