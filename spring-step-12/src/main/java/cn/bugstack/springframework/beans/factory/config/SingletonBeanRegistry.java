package cn.bugstack.springframework.beans.factory.config;

/**
 * 定义一个用于注册和获取单例对象的接口
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void registrySingleton(String beanName, Object singletonObject);
}
