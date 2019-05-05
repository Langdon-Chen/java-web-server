package com.langdon.tinywebserver.mvc;

import com.alibaba.fastjson.JSONObject;

public class Model {
    private JSONObject data ;


    public Model(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
