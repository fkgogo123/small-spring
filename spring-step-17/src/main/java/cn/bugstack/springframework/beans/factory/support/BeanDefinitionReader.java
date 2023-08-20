package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.core.io.ResourceLoader;

/**
 * 用于Bean定义的读取和注册的接口。
 */
public interface BeanDefinitionReader {

    // 存储definition的哈希容器
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resouce) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    /**
     * 根据 location 来判断使用哪个资源加载器
     * @param location
     * @throws BeansException
     */
    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;

}
