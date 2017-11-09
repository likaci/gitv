package com.tvos.apps.utils.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintsAnnotation {
    String checkIn() default "";

    String defaultValue() default "";

    Constraints[] nullParamsConstraints() default {};
}
