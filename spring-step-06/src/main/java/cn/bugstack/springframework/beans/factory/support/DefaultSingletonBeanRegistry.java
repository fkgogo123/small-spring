package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现注册和获取单例对象的接口
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registrySingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);

    }
}
