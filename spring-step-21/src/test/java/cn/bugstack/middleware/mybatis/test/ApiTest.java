package cn.bugstack.middleware.mybatis.test;

import cn.bugstack.middleware.mybatis.*;
import cn.bugstack.middleware.mybatis.test.dao.IUserDao;
import cn.bugstack.middleware.mybatis.test.po.User;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public class ApiTest {

    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;

        try {
//            reader = Resources.getResourceAsReader(resource);
//            DefaultSqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
            InputStream inputStream = new DefaultResourceLoader().getResource(resource).getInputStream();
            DefaultSqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlMapper.openSession();

            try {
                User user = session.selectOne("cn.bugstack.middleware.mybatis.test.dao.IUserDao.queryUserInfoById", 1L);
                System.out.println(JSON.toJSONString(user));
            } finally {
                session.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            InputStream inputStream = new DefaultResourceLoader().getResource(resource).getInputStream();
            DefaultSqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(inputStream);

            SqlSession session = sqlMapper.openSession();
            try {
                User req = new User();
                req.setUserId("184172133");
                List<User> userList = session.selectList("cn.bugstack.middleware.mybatis.test.dao.IUserDao.queryUserList", req);
                System.out.println(JSON.toJSONString(userList));
            } finally {
                session.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_ClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        BeanFactory beanFactory = applicationContext;
        IUserDao userDao = beanFactory.getBean("IUserDao", IUserDao.class);
        User user = userDao.queryUserInfoById(1L);
        System.out.println("测试结果：{}" + JSON.toJSONString(user));
//        logger.info("测试结果：{}", JSON.toJSONString(user));
    }
}
