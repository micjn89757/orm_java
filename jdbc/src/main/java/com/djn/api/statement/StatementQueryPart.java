package com.djn.api.statement;



import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author djn
 * Description: 使用statement做静态查询
 *
 * TODO:
 *      运行下面的例子时先调一下jdbc.properties中的dbUrl
 */
public class StatementQueryPart {
    /**
     * TODO:
     *      常用api: DiverManager, Connection, Statement, ResultSet
     */
    public static void main(String[] args) {
        InputStream fis = null;
        try {
            /**
             * 加载resource下的配置文件
             */
            final Properties properties = new Properties();
            ClassLoader classLoader = StatementQueryPart.class.getClassLoader();
            fis = classLoader.getResourceAsStream("jdbc.properties");
            properties.load(fis);
            String driver = properties.getProperty("driver");
            //1. 注册驱动
            /**
             * TODO:
             *  依赖: 驱动版本8+ com.mysql.cj.jdbc.Driver
             *       驱动版本5+ com.mysql.jdbc.Driver
             */
            // 方式一: 这个会导致注册两次驱动
            // DriverManager.registerDriver(new Driver());
            /**
             * 实际上只要触发类加载静态代码块即可
             * 1. new关键字
             * 2. 调用静态属性
             * 3. 调用静态方法
             * 4. 接口 包含1.8 新特性 default关键字
             * 5. 反射 【Class.forName() 类名.class 以及调用类加载器】
             * 6. 子类调用会触发父类的静态代码块
             * . 触发类的入口方法main
             */

            // 推荐这种方式，这样可以通过变动参数，就加载不同版本/数据库的驱动
            // 可以把参数提取到外部的配置文件
            Class.forName(driver);

            //2. 获取连接
            /**
             * TODO:
             *  和数据库创建连接，与Navicat类似
             *  需要如下信息:
             *  数据库服务器的ip地址,本地就是127.0.0.1
             *  数据库服务器端口号3306
             *  账号
             *  密码
             *  连接的数据库的名称
             *
             *  getConnection参数:
             *      url:
             *          格式为 jdbc:数据库厂商名://ip地址:part/数据库名
             *          如果数据库在本地(localhost/127.0.0.1)并且端口是3306，可以简写为 jdbc:mysql:///数据库名
             *          url可以携带参数，8.0.25+版本驱动可以自动识别时区, serverTimezone=Asia/Shanghai可以省略
             *          8+版本默认使用utf-8,useUnicode=true&characterEncoding=utf8&useSSL=true可以省略
             *          如果8版本在之前要在url后加以上两个参数serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=true
             *      username 数据库软件的账号
             *      password 数据库软件的密码
             *
             *  getConnection是一个重载方法
             *  可以只传入url, 这个url会附带很多可选信息,jdbc:数据库厂商名://ip地址:part/数据库名?user=[value]&password=[value]...
             *  或者传入url和info(是一个properties)
             *      properties 必须有user:账号信息和password:密码信息
             */
            String dbURL = properties.getProperty("dbUrl");
            Connection connection = DriverManager.getConnection(dbURL, properties);

            //3. 创建发送sql语句对象
            // statement可以发送SQL语句到数据库并获取返回结果
            Statement statement = connection.createStatement();

            //4. 发送sql语句，获取返回结果
            /**
             * 着重executeQuery和excuteUpdate
             */
            String last_name = "King";
            String em = "SKING";
            String sql = "select employee_id, last_name, email, salary from employees WHERE last_name = '" + last_name + "' AND email = '" + em + "';";
            // DQL常使用
            ResultSet resultSet = statement.executeQuery(sql);
            // DML语句会返回影响的行数, 非DML会返回0, DQL会返回结果集这个就会抛异常
//            int i = statement.executeUpdate(sql);

            //5. 结果集解析
            // 先判断有没有下一行数据，有就可以获取
            // ResultSet -> 逐行获取数据 -> 列获取
            // ResultSet维护了一个光标指向行，可以往前往后移动
            // 只需要记住next, 如果出现next不能满足，那是其他的问题
            while (resultSet.next()) {
                // 如果查询时列规定了别名就使用别名，还可以通过index获取列值(从1开始)
                int id = resultSet.getInt("employee_id");
                String name  = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                double salary = resultSet.getDouble("salary");
                System.out.println("id:" + id + " name:" + name + " email:" + email + " salary:" + salary);
            }


            //6. 资源关闭
            // 从内向外进行关闭
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
