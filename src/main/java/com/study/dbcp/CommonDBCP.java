package com.study.dbcp;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.sql.*;

/**
 * Created by tianyuzhi on 15/5/17.
 * https://commons.apache.org/proper/commons-dbcp/apidocs/org/apache/commons/dbcp2/package-summary.html#package_description
 * https://svn.apache.org/repos/asf/commons/proper/dbcp/trunk/doc/PoolingDataSourceExample.java
 */
public class CommonDBCP {
    private static String URL = "jdbc:mysql://10.111.0.70:3306/account";
    private static String USER = "account";
    private static String PASSWORD = "&t8DFylw3(r";

    private static PoolingDataSource dataSource;

    public static void shutdownDriver() throws Exception {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.closePool("example");
    }
    public static void printDriverStats() throws Exception {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        ObjectPool<? extends Connection> connectionPool = driver.getConnectionPool("example");

        System.out.println("NumActive: " + connectionPool.getNumActive());
        System.out.println("NumIdle: " + connectionPool.getNumIdle());
    }
    public static void printRS(ResultSet rs) throws SQLException {
        try {
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
        } finally {

        }
    }
    public static PoolingDataSource setupDataSource(String url, String user, String password) {
        ConnectionFactory connectionFactory=new DriverManagerConnectionFactory(url, user, password);
        //
        // Next we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        //
        // Now we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);

        // Set the factory's pool property to the owning pool
        poolableConnectionFactory.setPool(connectionPool);

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource<PoolableConnection> dataSource =
                new PoolingDataSource<>(connectionPool);

        return dataSource;
    }

    public void testDataSource() throws SQLException {
        PoolingDataSource dataSource = setupDataSource(URL, USER, PASSWORD);
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery("select * from PUSH_FOR_ANDROID");
            printRS(rs);
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e) { }
            try { if (stmt != null) stmt.close(); } catch(Exception e) { }
            try { if (connection != null) connection.close(); } catch(Exception e) { }
        }
        System.out.println("test datasource dones");
    }


    public static void setupDriver(String url, String user, String password) throws Exception {
        //
        // First, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        ConnectionFactory connectionFactory=new DriverManagerConnectionFactory(url, user, password);


        //
        // Next, we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        //
        // Now we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);

        // Set the factory's pool property to the owning pool
        poolableConnectionFactory.setPool(connectionPool);

        //
        // Finally, we create the PoolingDriver itself...
        //
        Class.forName("org.apache.commons.dbcp2.PoolingDriver");
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

        //
        // ...and register our pool with it.
        //
        driver.registerPool("example",connectionPool);

        //
        // Now we can just use the connect string "jdbc:apache:commons:dbcp:example"
        // to access our pool of Connections.
        //
    }


    public void testDriver() throws Exception {
        setupDriver(URL, USER, PASSWORD);
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:example");
            stmt = connection.createStatement();
            rs = stmt.executeQuery("select * from PUSH_FOR_ANDROID");
            printRS(rs);
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e) { }
            try { if (stmt != null) stmt.close(); } catch(Exception e) { }
            try { if (connection != null) connection.close(); } catch(Exception e) { }
        }
        shutdownDriver();
        System.out.println("test driver done");
    }

    public static void main(String[] args) throws Exception {
        CommonDBCP commonDBCP = new CommonDBCP();
        //commonDBCP.testDataSource();
        commonDBCP.testDriver();
    }


}
