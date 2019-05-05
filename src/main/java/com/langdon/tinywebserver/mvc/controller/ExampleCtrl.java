package com.langdon.tinywebserver.mvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.langdon.annotation.Controller;
import com.langdon.annotation.Qualifier;
import com.langdon.annotation.RequestMapping;
import com.langdon.tinywebserver.http.impl.BasicHttpRequest;
import com.langdon.tinywebserver.http.impl.BasicHttpResponse;
import com.langdon.tinywebserver.http.HttpMethod;
import com.langdon.tinywebserver.mvc.Model;
import com.langdon.tinywebserver.mvc.View;
import com.langdon.tinywebserver.mvc.service.ExampleService;

/**
 * @desc
 * @create by langdon on 2018/4/9-下午11:38
 **/
@Controller("/example")
public class ExampleCtrl {

    @Qualifier("ExampleServiceImpl")
    ExampleService exampleService;

    // 这个 method 怎么判断？在路由的时候就要解决
    @RequestMapping(value = "/get",method = HttpMethod.GET)
    public Model getPet(BasicHttpRequest request, BasicHttpResponse response){
        JSONObject data = new JSONObject();
        data.put("data",exampleService.get());
        return new Model(data);
    }

    @RequestMapping(value = "/index",method = HttpMethod.GET)
    public View hello(BasicHttpRequest request, BasicHttpResponse response){
        return new View("index.html");
    }
}
