# JDBC(Java Database Connectivity)

JDBC由java语言的规范(接口)和各个数据库厂商实现的驱动(jar)组成
JDBC是一种典型的面向接口编程
jdk下的jdbc规范接口存储在java.sql和javax.sql包中

## jdbc api使用路线
1. 静态SQL(没有动态值语句), 不需要动态语句的时候可以使用
> DriverManger -> Connection -> Statement -> ResultSet

存在的问题: 
- 这种方式在动态拼接字符串的过程中可能发生注入攻击
- SQL语句需要拼接字符串比较麻烦
- 只能拼接字符串类型，其他数据库类型无法处理


2. 预编译SQL路线(有动态值语句)
> DriverManager -> Connection -> PreparedStatement -> ResultSet

通过对sql字符串中占位符？进行替换编译sql， 能够提前获取sql的结构防止注入攻击
推荐使用

3. 执行标准存储过程SQL路线
> DriverManager -> Collection -> CallableStatement -> ResultSet

## 具体核心类和接口
api路线1常用的方法和参数细节详见StatementQueryPart.java
- **DriverManager**
    1. 将第三方数据库厂商的实现驱动jar注册到程序中
    2. 可以根据数据库连接信息获取connection
- **Connection**
    - 和数据库建立的连接,在连接对象上,可以多次执行数据库curd动作
    - 可以获取Statement和PreparedStatement,CallableStatement对象
- Statement | **PreparedStatement** | CallableStatement
    - 具体发送SQL语句到数据库管理软件的对象
    - 不同发送方式稍有不同,**PreparedStatement **使用为重点
- Result
    - **面向对象思维的产物**(抽象成数据库的查询结果表)
    - 存储DQL查询数据库结果的对象
    - 需要我们进行解析,获取具体的数据库数据

## jdbc基本使用步骤
1. 注册驱动
2. 获取连接
3. 创建发送sql语句对象
4. 发送sql语句，获取返回结果
5. 结果集解析
6. 资源关闭