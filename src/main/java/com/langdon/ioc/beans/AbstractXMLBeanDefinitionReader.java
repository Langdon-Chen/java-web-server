package com.langdon.ioc.beans;


import com.langdon.ioc.beans.io.ResourceLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从配置中读取BeanDefinitionReader
 * 
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractXMLBeanDefinitionReader implements BeanDefinitionReader {

    private Map<String,BeanDefinition> registry;

    private ResourceLoader resourceLoader;

    protected AbstractXMLBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new ConcurrentHashMap<>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
