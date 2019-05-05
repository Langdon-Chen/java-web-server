package com.langdon.tinywebserver.dispatcher;

import com.langdon.annotation.Controller;
import com.langdon.annotation.Qualifier;
import com.langdon.annotation.RequestMapping;
import com.langdon.annotation.Service;
import com.langdon.conf.WebConfiguration;
import com.langdon.tinywebserver.http.HttpMethod;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @desc
 * @create by langdon on 2019/1/2-10:16 AM
 **/

public abstract class AbstractPathPathDispatcher implements PathDispatcherService {

    protected   final Logger logger = Logger.getLogger(getClass());

    protected static final String DefaultScanPackage = WebConfiguration.getBaseScanPackage();

    protected List<String> classNames ;

    protected Map<String,Object> controllerInstanceMap ;

    protected Map<String,Object> serviceInstanceMap ;

    protected Map<String, Method> rooterMap ;

    protected Map<String,HandlerMethod> handlerMethodMap;

    public AbstractPathPathDispatcher(String basePackage){
        classNames = new CopyOnWriteArrayList<>();
        controllerInstanceMap = new ConcurrentHashMap<>();
        serviceInstanceMap = new ConcurrentHashMap<>() ;
        rooterMap = new ConcurrentHashMap<>() ;
        handlerMethodMap = new ConcurrentHashMap<>();
        //找bean
        if (basePackage.indexOf(",") > 0){
            String[] bp = basePackage.split(",");
            for (String p : bp)
                scanBeans(p);
        }else scanBeans(basePackage);
        //生成并且注册bean
        try {
            instanceAndRegister();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //根据注解注入bean
        try {
            springDi();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //根据注解路由注册bean方法
        mvc();
    }

    /**
     * 根据packageName扫描所有类，保存其真实路径
     */
    public void scanBeans(String packageName) {
        String classpath = this.getClass().getResource("/").getPath();
        String path = classpath + packageName.replace(".",File.separator);
        File file = new File(path);
        File [] subFiles = file.listFiles();
        if (subFiles != null){
            for (File f : subFiles){
                if (f.isDirectory()){
                    scanBeans(packageName + "." + f.getName());
                } else{
                    classNames.add(packageName + "." + f.getName());
                }
            }
        }

    }

    /**
     * 遍历class, 实例化带有  {@link Controller} 与  {@link Service} 注解的类
     */
    public void instanceAndRegister() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (classNames.size() ==0){
            return;
        }
        for (String className : classNames){
            String className2 = className.replace(".class","");
            logger.info("trying to instance class : "+ className2);
            Class clazz = Class.forName(className2);
            if (clazz.isAnnotationPresent(Controller.class)){
                Object instance = clazz.newInstance();
//                String key =  ((Controller)clazz.getAnnotation(Controller.class)).value();
                String key = clazz.getName();// complete class name
                controllerInstanceMap.put(key,instance);
                logger.info("instancing controller " + key);
            }else if (clazz.isAnnotationPresent(Service.class)){
                Object instance = clazz.newInstance();
                String key =  ((Service)clazz.getAnnotation(Service.class)).value();
                serviceInstanceMap.put(key,instance);
                logger.info("instancing service "+ key);
            }
        }
        logger.info("done instance");
    }

    /**
     * 根据注解,注入实例
     */
    public void springDi() throws IllegalAccessException {
        if (controllerInstanceMap.size()==0){
            return;
        }
        for (Map.Entry<String,Object> entry: controllerInstanceMap.entrySet()){
            Object instance = entry.getValue();
            Field[] fields =  instance.getClass().getDeclaredFields(); // getFields()->只能获得public field
            for (Field field : fields){
                if (field.isAnnotationPresent(Qualifier.class)){
                    String key = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);//防止private
                    field.set(entry.getValue(),serviceInstanceMap.get(key));
                    logger.info("injecting "+serviceInstanceMap.get(key).toString() +" to " + entry.getKey() +"."+field.getName());
                }
            }
        }
        logger.info("dependence injection done");
    }

    /**
     * build the handleMap
     */
    public void mvc(){
        if (controllerInstanceMap.size()==0){
            return;
        }
        for (Map.Entry<String,Object> entry : controllerInstanceMap.entrySet()){
            Class<?> c  = entry.getValue().getClass();
            if(c.isAnnotationPresent(Controller.class)){
                String ctrlUri = c.getAnnotation(Controller.class).value();
                Method[] methods = c.getMethods();
                if (methods!=null){
                    for (Method method : methods){
                        if (method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping requestMapping  = method.getAnnotation(RequestMapping.class);
                            String reqUri = requestMapping.value();
                            HttpMethod httpMethod = requestMapping.method();
                            String pathUrl = constructPathUri(ctrlUri,reqUri);
                            HandlerMethod handlerMethod = new HandlerMethod(pathUrl,method,httpMethod);
                            handlerMethodMap.put(pathUrl,handlerMethod);
                            rooterMap.put(pathUrl,method);
                            logger.info("router "+pathUrl + " -> " + c.getSimpleName()+"." +method.getName());
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param ctrlUri
     * @param reqUri
     * @return
     */
    private String constructPathUri(String ctrlUri,String reqUri){
        String pathUrl ;
        if (ctrlUri.startsWith("/")){
            pathUrl = reqUri.startsWith("/") ?  ctrlUri + reqUri :  ctrlUri + "/" + reqUri;
        }
        else pathUrl = "/" +  ctrlUri + "/" +reqUri ;
        return pathUrl;
    }
}
