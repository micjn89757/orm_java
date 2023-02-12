package com.djn.api.preparedstatement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author djn
 * Description: 使用预编译statement完成指定用户查询
 *
 * TODO:
 *      运行下面的例子时先调一下jdbc.properties中的dbUrl
 */
public class PSEmpQueryPart {
    private static final Properties properties = new Properties();
    public static void main(String[] args) {

        // 加载配置
        ClassLoader classLoader = PSEmpQueryPart.class.getClassLoader();
        try(InputStream is = classLoader.getResourceAsStream("jdbc.properties");) {

            properties.load(is);


            // 注册驱动
            String driver = properties.getProperty("driver");
            Class.forName(driver);

            // 获取连接
            String jdbcUrl = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(jdbcUrl, properties);

            // 假设获取输入到的last_name和email
            String name = "De Haan";
            String email = "LDEHAAN";

            // 编写sql语句结构, 动态值部分使用占位符?
            // 注意: ?只能代表值，不能代表表名等
            // 创建preparedstatement对象，传入动态值，动态值会替换掉?
            String sql = "select * from employees where last_name = ? and email = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 单独对占位符进行替换赋值
            /**
             * 参数1：index 占位符的位置从左向右，从1开始
             * 参数2：object 占位符的值 可以设置任何类型的数据, 使得拼接类型更加丰富, 防止了SQL注入
             */
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, email);

            // 发送SQL语句，获取结果集
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String phoneNumber = resultSet.getString("phone_number");
                System.out.println(phoneNumber);
            }


            // 关闭资源
            resultSet.close();
            preparedStatement.close();
            connection.close();



        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
