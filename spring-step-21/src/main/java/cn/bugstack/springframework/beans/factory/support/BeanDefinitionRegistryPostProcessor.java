package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * 服务于orm框架
 *
 * 在容器启动时，扫描mapper文件夹，为每一个dao接口类生成 代理对象的BeanDefinition
 *
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException;
}
