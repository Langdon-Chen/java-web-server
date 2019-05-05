package com.langdon.ioc.beans;

import com.langdon.ioc.beans.web.AnnotationBeanDefinitionReader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc  根据注解初始化bean
 * @create by langdon on 2019/1/2-1:09 AM
 **/

public abstract class AbstractAnnotationBeanDefinitionReader {
    private Map<String,Object> registry;

    protected String [] basePackages;

    public Map<String, Object> getRegistry() {
        return registry;
    }
    public AbstractAnnotationBeanDefinitionReader(String... basePackages){
        this.registry = new ConcurrentHashMap<>();
        try {
            if (basePackages==null || basePackages.length==0){
                throw new IllegalStateException("basePackages can not be null !");
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }
    public void loadBeans()throws Exception{
        AnnotationBeanDefinitionReader annotationBeanDefinitionReader = new AnnotationBeanDefinitionReader(basePackages);
        annotationBeanDefinitionReader.loadBeans();
    }
}
