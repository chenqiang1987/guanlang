package com.twc.guanlang.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 参数检测
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TwcParamCheck {

    boolean required() default true;

    int length_min() default 1;

    int length_max() default 255;

    boolean require_tel() default false;

    boolean require_Number() default false;

}
