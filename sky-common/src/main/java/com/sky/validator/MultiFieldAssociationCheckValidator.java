package com.sky.validator;

import com.sky.annotation.validation.MultiFieldAssociationCheck;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultiFieldAssociationCheckValidator implements ConstraintValidator<MultiFieldAssociationCheck, Object> {

    private String when;
    private String must;
    private String errorField;

    @Override
    public void initialize(MultiFieldAssociationCheck constraintAnnotation) {
        this.when = constraintAnnotation.when();
        this.must = constraintAnnotation.must();
        this.errorField = constraintAnnotation.errorField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(when) || !StringUtils.hasText(must)) {
            // spel表达式为空串时直接不校验
            return true;
        }

        ExpressionParser parser = new SpelExpressionParser(); //  创建spel表达式解析器
        StandardEvaluationContext ctx = new StandardEvaluationContext(value);  // 创建对象上下文

        // SpelEvaluationException 出现这个异常代表传入的spel表达式错误
        Expression expressionWhen = parser.parseExpression(when);
        Boolean result = expressionWhen.getValue(ctx, Boolean.class);
        if (Boolean.TRUE.equals(result)) {
            // result为true说明满足了when的条件
            // 接下来继续判断，必须满足must条件才符合
            Expression expressionMust = parser.parseExpression(must);
            result = expressionMust.getValue(ctx, Boolean.class);
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(errorField)
                    .addConstraintViolation();
            return Boolean.TRUE.equals(result);
        }
        return true;
    }
}
