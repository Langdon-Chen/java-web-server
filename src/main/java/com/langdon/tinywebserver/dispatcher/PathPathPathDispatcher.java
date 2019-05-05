package com.langdon.tinywebserver.dispatcher;


import com.langdon.tinywebserver.http.HttpMethod;
import com.langdon.tinywebserver.http.HttpRequest;
import com.langdon.tinywebserver.http.HttpResponse;
import com.langdon.tinywebserver.http.HttpStatusCode;
import com.langdon.tinywebserver.http.impl.BasicHttpRequest;
import com.langdon.tinywebserver.http.impl.BasicHttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @desc deal with httpRequest
 * @create by langdon on 2019/1/2-12:00 AM
 **/

public class PathPathPathDispatcher extends AbstractPathPathDispatcher {

    public PathPathPathDispatcher(){
        this(DefaultScanPackage);
    }

    public PathPathPathDispatcher(String basePackage){
        super(basePackage);
    }
    /**
     * according to the requestUri , invoke the related method ,
     * set httpResponse as view or data on the basis of method's returnType
     * @param request
     * @param response
     * @return
     */
    public HttpResponse handleRequest(BasicHttpRequest request, BasicHttpResponse response){
        String pathUrl = request.getRequestUri();
        HttpMethod httpMethod = request.getHttpMethod();
        HandlerMethod handlerMethod = this.handlerMethodMap.get(pathUrl);
        if (handlerMethod==null || httpMethod != handlerMethod.getHttpMethod()){
            return null;
        }
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod);
        Class<?> c = handlerMethod.getDeclaringClass();
        Object instance = this.controllerInstanceMap.get(c.getName());
        try{
            invocableHandlerMethod.invokeAndHandle(instance,request,response);
        }catch (IllegalAccessException e) {
            logger.error(e.toString(),e);
        } catch (InvocationTargetException e) {
            logger.error(e.toString(),e);
        }
        return response;
    }




}
