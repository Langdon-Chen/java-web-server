package com.langdon.tinywebserver.dispatcher;

import com.langdon.conf.WebConfiguration;
import com.langdon.tinywebserver.http.HttpRequest;
import com.langdon.tinywebserver.http.HttpStatusCode;
import com.langdon.tinywebserver.http.impl.BasicHttpResponse;
import com.langdon.tinywebserver.mvc.Model;
import com.langdon.tinywebserver.mvc.ModelAndView;
import com.langdon.tinywebserver.mvc.View;
import com.langdon.tinywebserver.www.Http;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLConnection;

/**
 * @desc
 * @create by langdon on 2019/1/3-3:19 PM
 **/

public class InvocableHandlerMethod extends HandlerMethod {

    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public Object doInvoke(Object instance, HttpRequest request, BasicHttpResponse response) throws InvocationTargetException, IllegalAccessException {
        //todo  pre handle

        if (logger.isTraceEnabled()){
            // log
        }
        return getMethod().invoke(instance, request, response);
    }

    public void invokeAndHandle(Object instance, HttpRequest request, BasicHttpResponse response)throws InvocationTargetException, IllegalAccessException {
        Object returnValue = doInvoke(instance,request,response);
        // 暂定：仅允许返回值有如下3个
        if (returnValue instanceof Model){
            response.setEntity(((Model) returnValue).getData().toString());
        }else if (returnValue instanceof View){
            // todo  读取在webapp中的html文件，写到response中
            View view = (View)returnValue;
            try{
                String requestFilePath = WebConfiguration.getWebappPath() + File.separator + view.getViewName();
                File requestFile = new File(requestFilePath);
                File rootDir = new File(WebConfiguration.getWebappPath());
                //这里打印的是计算机上该文件的访问路径
                logger.info("requestUri.file.canoicalPath: "+requestFile.getCanonicalPath());
                if (!requestFile.getCanonicalPath().startsWith(rootDir.getCanonicalPath())) {
                    response.setStatusCode(HttpStatusCode.FORBIDDEN);
                    return;
                }
                if (requestFile.exists()){
        			response.setStatusCode(HttpStatusCode.OK);
        			try(InputStream inputStream  = new FileInputStream(requestFile)) {
        				byte [] fileContent;
                        fileContent = new byte[(int) requestFile.length()];
                        inputStream.read(fileContent); //将文件内容写入fileContent
        				response.setEntity(fileContent);
        				// guess and set the content type
        				response.getHeaders().put(Http.CONTENT_TYPE, URLConnection.guessContentTypeFromName(requestFile.getAbsolutePath()));
        			}
        			catch (FileNotFoundException e) {
        			    // 返回404
        				response.setStatusCode(HttpStatusCode.NOT_FOUND);
                        requestFilePath =WebConfiguration.getWebappPath() + File.separator+  "404.html";
                        requestFile = new File(requestFilePath);
                        try(InputStream inputStream  = new FileInputStream(requestFile)) {
                            byte [] fileContent;
                            fileContent = new byte[(int) requestFile.length()];
                            inputStream.read(fileContent); //将文件内容写入fileContent
                            response.setEntity(fileContent);
                        }
        			}
                }
            }
            catch (IOException e1)
            {
                response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
            }
        }else if (returnValue instanceof ModelAndView){
            // todo
        }else {
            throw new IllegalStateException("Unknown return value");
        }

    }
}
