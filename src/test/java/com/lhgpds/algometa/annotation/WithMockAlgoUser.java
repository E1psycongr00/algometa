package com.lhgpds.algometa.annotation;

import com.lhgpds.algometa.internal.member.entity.vo.Role;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockAlgoUserSecurityContextFactory.class)
public @interface WithMockAlgoUser {

    long id() default 1L;
    String email() default "helloworld@gmail.com";
    String nickname() default "개똥이";
    String image() default "s3s3s3";
    Role role() default Role.USER;

}
