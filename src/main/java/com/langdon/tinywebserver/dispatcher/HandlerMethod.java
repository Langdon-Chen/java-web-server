package com.langdon.tinywebserver.dispatcher;

import com.langdon.tinywebserver.http.HttpMethod;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * @desc
 * @create by langdon on 2019/1/3-11:19 AM
 **/

public class HandlerMethod {

    protected  final Logger logger = Logger.getLogger(getClass());

    private Class<?> returnType ;

    private String pathUrl ;

    private Method method ;

    private Class<?> declaringClass;

    /**
     * {@link com.langdon.annotation.RequestMapping}
     */
    private HttpMethod httpMethod;

    public HandlerMethod(HandlerMethod handlerMethod){
        this.pathUrl = handlerMethod.getPathUrl();
        this.method = handlerMethod.getMethod();
        this.declaringClass = handlerMethod.getDeclaringClass();
        this.httpMethod = handlerMethod.getHttpMethod();
        returnType =  handlerMethod.getReturnType();
    }

    public HandlerMethod(String pathUrl,Method method){
        this(pathUrl,method,HttpMethod.GET);
    }

    public HandlerMethod(String pathUrl,Method method,HttpMethod httpMethod){
        this.pathUrl = pathUrl;
        this.method = method;
        this.declaringClass = method.getDeclaringClass();
        this.httpMethod = httpMethod;
        returnType =  method.getReturnType();
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
