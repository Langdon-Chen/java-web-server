package com.langdon.annotation;

import java.lang.annotation.*;

/**
 * @desc qualifier注解,定义注入的对象
 * @create by langdon on 2018/4/9-下午11:36
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) //生效解释的时候为运行时候
@Documented //可能有文档,暂时没有
public @interface Qualifier {
    String value() default "";
}
