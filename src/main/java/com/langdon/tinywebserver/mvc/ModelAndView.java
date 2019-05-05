package com.langdon.tinywebserver.mvc;

import java.util.Map;

/**
 * 暂定所有的controller的返回值是 ModelAndView
 */
public class ModelAndView {
    private View view ;

    private Model model;


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
