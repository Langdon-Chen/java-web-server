package com.langdon.ioc.context;

import com.langdon.ioc.beans.web.AnnotationBeanDefinitionReader;

/**
 * @desc
 * @create by langdon on 2019/1/2-12:39 AM
 **/

public class AnnotationConfigWebApplicationContext {

    public AnnotationConfigWebApplicationContext(String... basePackages){
        try {
            refresh(basePackages);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void refresh(String... basePackages){
        AnnotationBeanDefinitionReader annotationBeanDefinitionReader = new AnnotationBeanDefinitionReader(basePackages);
        annotationBeanDefinitionReader.loadBeans();
    }
}
