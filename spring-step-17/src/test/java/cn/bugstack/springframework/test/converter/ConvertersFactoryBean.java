package cn.bugstack.springframework.test.converter;

import cn.bugstack.springframework.beans.factory.FactoryBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    /**
     * getObjectForBeanInstance，为factoryBean生成代理对象 。
     * 在createBean全部完成之后，执行该步骤。
     *
     * @return
     * @throws Exception
     */
    @Override
    public Set<?> getObject() throws Exception {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
