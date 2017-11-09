package com.xcrash.crashreporter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageAnnotation {
    boolean isEncode() default true;

    String name() default "";

    String requestUrl() default "";
}
