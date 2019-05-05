package com.langdon.tinywebserver.mvc.service.impl;


import com.langdon.annotation.Service;
import com.langdon.tinywebserver.mvc.service.ExampleService;

/**
 * @desc
 * @create by langdon on 2018/4/9-下午11:40
 **/
@Service("ExampleServiceImpl")  // remember to add
public class ExampleServiceImpl implements ExampleService {
    @Override
    public String get() {
        return "Tiny Web Server By Langdon ; \n If you like this project , please fork and develop it to your project !";
    }
}
