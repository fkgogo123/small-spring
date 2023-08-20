package cn.bugstack.springframework.context.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 继承自 AbstractApplicationContext, 主要负责资源加载流程中的 BeanFactory创建与xml资源的扫描
 *
 * loadBeanDefinitions() 放在子类AbstractXmlApplicationContext中实现。
 *
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        // 刷新 容器
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        // 创建一个 BeanFactory。DefaultBeanFactory是为spring框架设计的支持组件，不暴露给用户。
        return new DefaultListableBeanFactory();
    }

    /**
     *
     * BeanDefinition的 加载 与 扩展
     *
     * @param beanFactory
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
