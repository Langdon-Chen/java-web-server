package com.langdon.annotation;

import com.langdon.tinywebserver.http.HttpMethod;

import java.lang.annotation.*;

/**
 * @desc
 * @create by langdon on 2018/4/9-下午11:37
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //生效解释的时候为运行时候
@Documented //可能有文档,暂时没有
public @interface RequestMapping {
    String value() default "";

    /**
     * for rest api
     */
    HttpMethod method() ;
}
