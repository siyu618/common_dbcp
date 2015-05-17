package com.study.dbcp;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by tianyuzhi on 15/5/17.
 */
public class SimpleJDBC {


    private java.sql.Connection connection;
    private java.sql.PreparedStatement preparedStatement;

    public SimpleJDBC() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://10.111.0.70:3306/account", "account", "&t8DFylw3(r");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeQuery(String sql, LinkedList<Object> params) {
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (params != null) {
                int i = 1;
                for (Object p : params) {
                    preparedStatement.setObject(i++, p);
                }
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        LinkedList<Object> params = new LinkedList<Object>();
        params.add("yidian");
        SimpleJDBC tool = new SimpleJDBC();
        ResultSet rs = tool.executeQuery("SELECT * FROM PUSH_FOR_ANDROID where appid = ?", params);
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
        tool.close();;


    }
}