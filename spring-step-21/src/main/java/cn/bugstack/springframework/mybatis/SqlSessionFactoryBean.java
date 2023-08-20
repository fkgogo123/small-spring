package cn.bugstack.springframework.mybatis;

import cn.bugstack.middleware.mybatis.DefaultSqlSession;
import cn.bugstack.middleware.mybatis.SqlSession;
import cn.bugstack.middleware.mybatis.SqlSessionFactory;
import cn.bugstack.middleware.mybatis.SqlSessionFactoryBuilder;
import cn.bugstack.springframework.beans.factory.FactoryBean;
import cn.bugstack.springframework.beans.factory.InitializingBean;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import cn.bugstack.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSession 工厂
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {

    private String resource;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource(this.resource);

        try (InputStream inputStream = resource.getInputStream()) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        return this.sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public void setResource(String resource) {
        this.resource = resource;
    }


}
