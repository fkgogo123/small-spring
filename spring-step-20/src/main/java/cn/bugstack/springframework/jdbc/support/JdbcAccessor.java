package cn.bugstack.springframework.jdbc.support;

import cn.bugstack.springframework.beans.factory.InitializingBean;
import cn.hutool.core.lang.Assert;

import javax.sql.DataSource;


public abstract class JdbcAccessor implements InitializingBean {

    private DataSource dateSource;

    public DataSource getDateSource() {
        return dateSource;
    }

    public void setDateSource(DataSource dateSource) {
        this.dateSource = dateSource;
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = getDateSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        if (getDateSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
    }
}
