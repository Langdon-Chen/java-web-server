package com.langdon.tinywebserver.mvc;

/**
 * 页面视图
 */
public class View {

    private String viewName ;

    public View(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
