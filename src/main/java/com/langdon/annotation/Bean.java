package com.langdon.annotation;

import java.lang.annotation.*;

/**
 * @desc
 * @create by langdon on 2018/5/25-上午10:43
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    boolean require() default true;
}
