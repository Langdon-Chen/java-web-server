package com.langdon.ioc.beans.web;

import com.langdon.annotation.Controller;
import com.langdon.annotation.Qualifier;
import com.langdon.annotation.Service;
import com.langdon.ioc.beans.AbstractAnnotationBeanDefinitionReader;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc
 * @create by langdon on 2019/1/2-1:20 AM
 **/

public class AnnotationBeanDefinitionReader extends AbstractAnnotationBeanDefinitionReader {

    private Set<String> classNames = new LinkedHashSet<>();

    private Map<String, Method> methodMap = new ConcurrentHashMap<>();

    public AnnotationBeanDefinitionReader(String... basePackages){
        super(basePackages);
    }

    @Override
    public void loadBeans(){
        //      找bean
        for (String p : this.basePackages)
            scanBeans(p);

        //      生成并且注册bean
        try {
            instanceAndRegister();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //      根据注解注入bean
        try {
            springDi();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描所有类
     * @param packageName
     */
    private void scanBeans(String packageName) {
        String urlPath = "/" + packageName.replaceAll("\\." ,"/" );
        URL url = this.getClass().getClassLoader().getResource(urlPath);
        assert url != null;
        String path = url.getPath();
        File file = new File(path);
        File [] subFiles = file.listFiles();
        if(subFiles!=null){
            for (File f : subFiles){
                if (f.isDirectory()){
                    scanBeans(packageName + "." + f.getName());
                } else{
                    System.out.println("class name : "+f.getName());
                    classNames.add(packageName + "." + f.getName());
                }
            }
        }
    }


    /**
     * 遍历class, 实例化带有  {@link Controller} 与  {@link Service} 注解的类
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void instanceAndRegister() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (classNames.size() ==0){
            return;
        }
        for (String className : classNames){
            Class clazz = Class.forName(className.replace(".class",""));
            if (clazz.isAnnotationPresent(Controller.class)){
                Object instance = clazz.newInstance();
                String key =  ((Controller)clazz.getAnnotation(Controller.class)).value();
                getRegistry().put(key,instance);
            }else if (clazz.isAnnotationPresent(Service.class)){
                Object instance = clazz.newInstance();
                String key =  ((Service)clazz.getAnnotation(Service.class)).value();
                getRegistry().put(key,instance);
            }else continue;
        }
    }

    /**
     * 根据注解,注入实例
     */
    private void springDi() throws IllegalAccessException {
        if (getRegistry().size()==0){
            return;
        }
        for (Map.Entry<String,Object> entry: getRegistry().entrySet()){
            Object instance = entry.getValue();
            Field[] fields =  instance.getClass().getDeclaredFields(); // getFields()->只能获得public field
            for (Field field : fields){
                if (field.isAnnotationPresent(Qualifier.class)){
                    String key = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);//防止private
                    //set(Object obj, Object value)
                    //@param obj the object whose field should be modified
                    //@param value the new value for the field of {@code obj}
                    field.set(entry.getValue(),getRegistry().get(key));
                }
            }
        }
    }

    public Map<String, Method> getMethodMap() {
        return methodMap;
    }
}
