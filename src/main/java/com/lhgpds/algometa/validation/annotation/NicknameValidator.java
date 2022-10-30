package com.lhgpds.algometa.validation.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {

    private int maxSize;

    @Override
    public void initialize(Nickname constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regex = String.format("^[a-zA-Z가-힣][a-zA-Z가-힣\\d]{2,%d}$", maxSize);
        Matcher matcher = Pattern.compile(regex).matcher(value);
        return matcher.find();
    }
}
