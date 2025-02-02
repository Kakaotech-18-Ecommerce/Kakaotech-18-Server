package com.kakaoteck.golagola.global.annotation.validator;

import com.kakaoteck.golagola.global.annotation.CheckMonth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckMonthValidator implements ConstraintValidator<CheckMonth, Integer> {

    @Override
    public void initialize(CheckMonth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer month, ConstraintValidatorContext context) {
        if (month == null || month < 1 || month > 12) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("월은 1 이상 12 이하 이어야 합니다.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
