package com.langdon.conf;

/**
 * @desc
 * @create by langdon on 2019/1/2-12:08 AM
 **/

public final class WebConfiguration extends AbstractConfiguration {

    private WebConfiguration(){}

    public static String getBaseScanPackage(){
        return getConfig("base.scan.package");
    }

    public static int getServerPort(){
        return Integer.parseInt(getConfig("server.port"));
    }

    public static String getServerName(){
        return getConfig("server.name");
    }
    public static String getServerVersion(){
        return getConfig("server.version");
    }
    public static String getServerCharset(){
        return getConfig("server.charset");
    }

    public static String getWebappPath(){
        return getConfig("web.root");
    }
}
