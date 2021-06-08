package com.twc.guanlang.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * this customize annotation class is used to
 *  * add english comment by myself
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface MyEnglishDoc {
    String subject() default "";

    String value() default "";
}
