package com.langdon.conf;

/**
 * @desc
 * @create by langdon on 2019/1/1-1:27 AM
 **/

public final class JDBCConfig extends AbstractConfiguration {

    public static String getJDBCUrl(){
        return getConfig("jdbc.url");
    }

    public static String getJDBCUsername(){
        return getConfig("jdbc.username");
    }

    public static String getJDBCPassword(){
        return getConfig("jdbc.password");
    }

    public static void main(String[] args) {
        System.out.println(JDBCConfig.getJDBCUrl());
    }

}
