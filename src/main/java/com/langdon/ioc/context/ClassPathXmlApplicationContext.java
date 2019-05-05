package com.langdon.ioc.context;


import com.langdon.ioc.beans.BeanDefinition;
import com.langdon.ioc.beans.factory.AbstractBeanFactory;
import com.langdon.ioc.beans.factory.AutowireCapableBeanFactory;
import com.langdon.ioc.beans.io.ResourceLoader;
import com.langdon.ioc.beans.xml.XmlXMLBeanDefinitionReader;

import java.util.Map;

/**
 * @author yihua.huang@dianping.com
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

	private String configLocation;

	public ClassPathXmlApplicationContext(String configLocation) {
		this(configLocation, new AutowireCapableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory){
		super(beanFactory);
		try {
			this.configLocation = configLocation;
			refresh();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void refresh() throws Exception {
		XmlXMLBeanDefinitionReader xmlBeanDefinitionReader = new XmlXMLBeanDefinitionReader(new ResourceLoader());
		xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);
		for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : xmlBeanDefinitionReader.getRegistry().entrySet()) {
			beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
		}
	}

}
