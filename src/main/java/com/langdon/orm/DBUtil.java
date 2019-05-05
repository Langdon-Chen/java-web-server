package com.langdon.orm;

/**
 * @desc
 * @create by langdon on 2019/1/1-1:22 AM
 **/

import com.langdon.conf.JDBCConfig;

import java.sql.*;

public class DBUtil {

    private static final String URL = JDBCConfig.getJDBCUrl();
    private static final String USER = JDBCConfig.getJDBCUsername();
    private static final String PASSWORD = JDBCConfig.getJDBCPassword();
    private static Connection conn=null;

    static {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2.获得数据库的连接
            conn=DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //将获得的数据库与java的链接返回（返回的类型为Connection）
    public static Connection getConnection(){
        return conn;
    }

    public static void main(String[] args)throws Exception {
        String s="select * from ub_post_category where 1=1";
        PreparedStatement pst=conn.prepareStatement(s);

        ResultSet rs=pst.executeQuery();
        //4.处理数据库的返回结果(使用ResultSet类)
        while(rs.next()){
            System.out.println(rs.getInt("category_id"));
        }
        //关闭资源
        rs.close();
        pst.close();
        conn.close();
    }
}