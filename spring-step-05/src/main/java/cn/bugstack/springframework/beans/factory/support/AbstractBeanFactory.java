package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;

/**
 * 定义顶层流程。
 *
 * createBean和属性注入：AbstractAutowireCapableBeanFactory
 * BeanDefinition加载：DefaultListableBeanFactory
 *
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 抽象类AbstractBeanFactory中，提供了getBean的工作流程以及getBean的具体方法实现。
     * 工作流程中涉及的一些步骤方法，提供一个抽象方法。最终在子类中完成具体的实现。
     * 体现出的设计模式：模板模式。
     * @param name
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    /**
     * 定义了 getBean方法，具体流程在子类中分别实现
     *
     * @param name
     * @param args
     * @return
     * @param <T>
     * @throws BeansException
     */
    protected <T> T doGetBean(String name, Object[] args) throws BeansException {
        Object bean = getSingleton(name);

        if (bean != null) {
            return (T) bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        return (T) createBean(name, beanDefinition, args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
