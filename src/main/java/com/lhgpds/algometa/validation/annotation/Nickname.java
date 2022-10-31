package com.lhgpds.algometa.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD,ElementType.FIELD})
@Constraint(validatedBy = NicknameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nickname {
    int maxSize() default 10;
    String message() default "닉네임은 영문,한글,숫자 3-10 글자, 첫글자는 영문,한글만 가능합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
