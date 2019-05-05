package com.langdon.conf;

import org.apache.commons.configuration.ConfigurationException;  
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 读取配置文件中的属性
 * @author langdon
 *
 */
public abstract class AbstractConfiguration {
	
	private final static String CONFIG_FILE_NAME = "config.properties";
	private static org.apache.commons.configuration.Configuration config ;
	static {
		try {
			AbstractConfiguration.config = new PropertiesConfiguration(CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getConfig(String name) {
		return config.getString(name);
	}
	
}
