package com.djn.api.preparedstatement;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * @author djn
 * Description: 使用preparedstatement进行dml操作
 */
public class PSDMLPart {
    /**
     * 获取配置
     */
    private static final ClassLoader classLoader = PSDMLPart.class.getClassLoader();
    private static final Properties properties = new Properties();
    private static String driver = null;

    private static String dbUrl = null;

    /**
     * 向emps表插入一条数据
     * @return 返回受影响的行数
     */
    public static int insert() {
        try (InputStream is = classLoader.getResourceAsStream("jdbc.properties");) {
            properties.load(is);
            // 注册驱动
            driver = properties.getProperty("driver");
            Class.forName(driver);

            // 获取连接
            dbUrl = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(dbUrl, properties);

            // 创建prepareStatement对象
            String sql = "INSERT INTO emps(employee_id, last_name, email, hire_date, job_id) VALUES(?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // 填充占位符
            preparedStatement.setInt(1, 655);
            preparedStatement.setString(2, "dddd");
            preparedStatement.setString(3, "1980846456@qq.com");
            preparedStatement.setString(4, "2023-02-12");
            preparedStatement.setString(5, "5");

            // 发送sql语句, 返回受影响的行数
            int effect_rows =  preparedStatement.executeUpdate();

            // 关闭pS对象和连接
            preparedStatement.close();
            connection.close();

            return effect_rows;
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }


    public static int update() {
        try (InputStream is = classLoader.getResourceAsStream("jdbc.properties");) {
            properties.load(is);

            // 注册驱动
            driver = properties.getProperty("driver");
            Class.forName(driver);

            // 获取连接
            dbUrl = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(dbUrl, properties);

            // 创建prepareStatement对象
            String sql = "UPDATE emps SET last_name = ? WHERE employee_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "demo");
            preparedStatement.setInt(2, 655);

            // 发送sql语句
            int effect_rows = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            return effect_rows;

        } catch (IOException | ClassNotFoundException | SQLException e) {
            return -1;
        }
    }

    public static int delete() {
        try (InputStream is = classLoader.getResourceAsStream("jdbc.properties");) {
            properties.load(is);

            // 注册驱动
            driver = properties.getProperty("driver");
            Class.forName(driver);

            // 获取连接
            dbUrl = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(dbUrl, properties);

            // 创建prepareStatement对象
            String sql = "DELETE FROM emps WHERE employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, 655);

            // 发送sql
            int effect_rows = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            return effect_rows;
        } catch (IOException | ClassNotFoundException | SQLException e) {
            return -1;
        }
    }

    /**
     * 查询
     * resultSet.getMetaData()可以获取元数据，就是列的信息(名称，下角标，列数量)
     */
    public static void query() {
        try (InputStream is = classLoader.getResourceAsStream("jdbc.properties");) {
            properties.load(is);

            // 注册驱动
            driver = properties.getProperty("driver");
            Class.forName(driver);

            // 获取连接
            dbUrl = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(dbUrl, properties);

            // 创建prepareStatement对象
            String sql = "SELECT * FROM emps";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 发送sql
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map> list = new ArrayList<>();

            // 获取列的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 列的数量
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                // 注意从1开始
                for (int i = 1; i <= columnCount; i++) {
                    // getColumnName是获取原始列名，如果查询的时候使用了别名就要用getColumnLabel才可以
                    String columnName = metaData.getColumnName(i);
                    String columnLabel = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(columnName);
                    // 获取列对应的SQL类型
                    int columnType = metaData.getColumnType(i);
                }
            }

            preparedStatement.close();
            connection.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
