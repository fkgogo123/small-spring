package cn.bugstack.springframework.context.support;

import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 主要负责流程中的 xml资源的加载
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    /**
     * 应用上下文类中定义的 loadBeanDefinitions方法
     * @param beanFactory
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    /**
     * 从入口上下文的类中取得配置信息的地址
     *
     * @return
     */
    protected abstract String[] getConfigLocations();
}
