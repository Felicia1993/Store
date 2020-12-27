package org.mybatis.demo.test;

import java.sql.*;

public class TestJDBC {
    static {
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        selectByStatement("root", "xieqiqi037005");
    }

    public static void selectByStatement(String name, String password) {
        try {
            //创建connection对象
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&characterEncoding=UTF-8&useSSL=false&");
            //使用connection创建statement或者preparedStatement类对象，用来执行sql语句
            Statement statement = connection.createStatement();
            //执行sql语句，并获得结果集存放ResultSet对象
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM USER WHERE user_name'"+name+"' and pass_word="+password);
            while (resultSet.next()) {
                String columnName1 = resultSet.getMetaData().getColumnName(1);
                String columnName12 = resultSet.getMetaData().getColumnName(2);
                System.out.println(columnName1 + ":" + resultSet.getString(1));
                System.out.println(columnName1 + ":" + resultSet.getString(1));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
