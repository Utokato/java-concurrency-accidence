package cn.llman.concurrency.classloader.chapter6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 打破双亲委派机制
 *
 * @date 2019/5/10
 */
public class JDBCTest {

    public static void main(String[] args) {

        try {
            // 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 获取连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phoenix", "root", "idyllo");
            // 获取SQL语句执行平台
            Statement statement = connection.createStatement();
            // 通过平台执行SQL语句，获取结果集
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            System.out.println("书籍ID\t" + "名称");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println(id + "\t" + name);
            }
            // 释放资源
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
