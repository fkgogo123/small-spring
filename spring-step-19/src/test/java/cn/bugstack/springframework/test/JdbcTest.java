package cn.bugstack.springframework.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {

    private DruidDataSource dataSource;
    private Connection connection;
    private Statement statement;

    @Before
    public void init() throws SQLException {
        dataSource = new DruidDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring?useSSL=false&serverTimezone=GMT");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void test_translation() throws SQLException {
        connection = dataSource.getConnection().getConnection();
        statement = connection.createStatement();

        connection.setAutoCommit(false);
        try {
            statement.execute("insert into user (id, userId, userHead, createTime, updateTime) values (1, '12332423', '01_50', now(), now())");
            statement.execute("insert into user (id, userId, userHead, createTime, updateTime) values (1, '12332423', '01_50', now(), now())");
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        }
        connection.commit();
    }
}
