package com.langdon;

import com.langdon.conf.WebConfiguration;
import com.langdon.tinywebserver.dispatcher.PathPathPathDispatcher;
import com.langdon.tinywebserver.server.HttpServer;
import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class Bootstrap
{
    private static final String APPLICATION_CONTEXT_PATH = "bean.xml";
    private static final String DEFAULT_SCAN_PACKAGES = WebConfiguration.getBaseScanPackage();
    private static final Logger logger = Logger.getLogger(Bootstrap.class);
    public static void main( String[] args ){
//        这里有两个deno，注释其中一个后运行另外一个即可

//1. tiny-spring
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_PATH);
//        HelloWorldService helloWorldService = (HelloWorldService) applicationContext.getBean("helloWorldService");
//        helloWorldService.helloWorld();

//2. tiny-web-server
        //初始化调度器
        logger.info("server starting...");
        PathPathPathDispatcher pathDispatcher = new PathPathPathDispatcher(DEFAULT_SCAN_PACKAGES);
        HttpServer server = new HttpServer(pathDispatcher);
        server.start();
    }

}
