package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.FactoryBean;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.bugstack.springframework.core.util.ClassUtils;
import cn.bugstack.springframework.core.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

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

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

//    protected <T> T doGetBean(String name, Object[] args) throws BeansException {
//        Object bean = getSingleton(name);
//        if (bean != null) {
//            return (T) bean;
//        }
//        BeanDefinition beanDefinition = getBeanDefinition(name);
//        return (T) createBean(name, beanDefinition, args);
//    }

    protected <T> T doGetBean(String name, Object[] args) throws BeansException {
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        // 创建了对象实例，并进行了属性填充，前置后置助理
        Object bean = (T) createBean(name, beanDefinition, args);
        // 如果是FactoryBean则生成代理对象。
        return (T) getObjectForBeanInstance(bean, name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 判断整个对象是否为一个 FactoryBean，不是则直接返回
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        // 是一个FactoryBean对象，进行缓存。
        Object object = getCachedObjectForFactoryBean(beanName);
        // 避免二次缓存
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }

        return object;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
