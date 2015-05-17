package com.study.dbcp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;


/**
 * Created by tianyuzhi on 15/5/17.
 */

public class TomcatDBCPExample {

    public static void main(String[] args) throws Exception {
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://10.111.0.70:3306/account");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("account");
        p.setPassword("&t8DFylw3(r");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);

        Connection con = null;
        try {
            con = datasource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from PUSH_FOR_ANDROID");
            int cnt = 1;
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) //跟踪显示各个列的名称
            {
                System.out.print(rs.getMetaData().getColumnName(i) + "\t");
            }
            System.out.println();

            while (rs.next()) { //跟踪显示各个列的值
                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                    System.out.print(rs.getObject(j) + "\t");
                }
                System.out.println();
            }
            rs.close();
            st.close();
        } finally {
            if (con!=null) {
                try {
                    con.close();
                } catch (Exception ignore) {
                }
            }
        }
        System.out.println("sss");
    }
}
