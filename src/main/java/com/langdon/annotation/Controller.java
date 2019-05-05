package com.langdon.annotation;

import java.lang.annotation.*;

/**
 * @desc controller注解
 * @create by langdon on 2018/4/9-下午11:30
 **/
@Target(ElementType.TYPE) //注解用于类
@Retention(RetentionPolicy.RUNTIME) //生效解释的时候为运行时候
@Documented //可能有文档,暂时没有
public @interface Controller {
    String value() default "";
}
