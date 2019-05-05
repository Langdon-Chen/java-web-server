package com.langdon.tinywebserver.dispatcher;

/**
 * @desc
 * @create by langdon on 2019/1/2-12:12 AM
 **/

public interface PathDispatcherService {

    /**
     * 扫描指定的包下所有class
     * @param basePackage
     */
    void scanBeans(String basePackage);

    void instanceAndRegister()throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    void springDi() throws IllegalAccessException;

    void mvc();

}
