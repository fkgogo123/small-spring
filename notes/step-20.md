实现功能：ORM框架，（对象关系映射），开发一个ORM中间件。
只需要定义DAO接口类，就可以关联到XML或注解配置的数据库操作语句。将查询结果自动转换成定义的po类。
避免了JDBC操作的复杂性，调用外部接口时，可以通过更简单的方式使用数据库。

ORM的核心流程：参数映射、SQL解析、执行、结果映射。

DefaultSqlSessionFactory.openSession()时，
会根据configuration配置（配置信息包含：链接、数据源、mapperElement）创建一个DefaultSqlSession。
在DefaultSqlSession定义了相关的查询语句执行操作。
查询语句的执行包含：预编译sql语句，预编译参数填充，结果映射。


涉及到两部分xml文件：
1.mybatis配置xml文件（mybatis-config-datasource.xml）
配置了数据源，和第二部分xml文件的路径。
2.mapper配置xml文件（User_Mapper.xml）
为相应的接口方法配置了对应的sql语句和参数，参数包括入参类型，返回值类型，以及预编译参数。


SqlSessionFactoryBuilder有以下方法：
build():
解析mybatis XML配置文件中的信息，调用以下三个方法，并初始化DefaultSqlSessionFactory。
parseConfiguration()：
获取XML文件中的元素，主要获取dataSource数据源和mappers的映射语句配置。
connection()：
链接数据库
mapperElement()：
解析XML文件（sql语句配置的xml文件，User_Mapper.xml）中SQL语句的配置, 
包括待 参数名称，参数类型，返回值类型，id即接口方法，命名空间即接口全限定名。

