package com.langdon.tinywebserver.dispatcher;

import com.langdon.tinywebserver.http.HttpMethod;

import java.lang.reflect.Method;

/**
 * @desc
 * @create by langdon on 2019/1/2-10:07 AM
 **/

public class RequestHandler {

    private String pathUrl ;

    private Method method ;

    private Class<?> declaringClass ;

    /**
     * {@link com.langdon.annotation.RequestMapping}
     */
    private HttpMethod httpMethod;

    public RequestHandler(String pathUrl,Method method ,Class<?> declaringClass,HttpMethod httpMethod){
        this.pathUrl = pathUrl;
        this.method = method;
        this.declaringClass = declaringClass;
        this.httpMethod = httpMethod;
    }

    public Method getMethod() {
        return method;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }


    @Override
    public boolean equals(Object another){
        if (this == another){
            return true;
        }
        if (another instanceof RequestHandler){
            RequestHandler anotherHandler = (RequestHandler)another;
            if (this.method.equals(anotherHandler.method)
            && this.httpMethod.equals(anotherHandler.httpMethod)
            && this.declaringClass.equals(anotherHandler.declaringClass)){
                return true;
            }
            else return false;
        }

        return false;
    }

    @Override
    public int hashCode(){
        int hash = 17;
        hash = hash * 31 + method.hashCode();
        hash = hash * 31 + declaringClass.hashCode();
        hash = hash * 31 + httpMethod.hashCode();
        return hash ;
    }
}
