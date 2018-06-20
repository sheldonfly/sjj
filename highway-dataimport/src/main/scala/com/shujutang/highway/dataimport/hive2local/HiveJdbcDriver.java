package com.shujutang.highway.dataimport.hive2local;

/**
 * Created by nancr on 2016/2/25.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveJdbcDriver {

    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.3.72:10000/default","spark","");
        Statement stmt = conn.createStatement();
//        String quary_sql = "select * from student";
        String quary_sql = "insert overwrite  local directory '/data/saprkhome/file/student' select * from student";
//        String quary_sql = "INSERT INTO student VALUES (7, 'wangwu')";
        boolean rs = stmt.execute(quary_sql);
        System.out.println("结果：" + rs);
//        ResultSet rs = stmt.executeQuery(quary_sql);
//        while(rs.next()){
//            System.out.println("id: " + rs.getInt(1) + "  name: " + rs.getString(2));
//        }
    }
}