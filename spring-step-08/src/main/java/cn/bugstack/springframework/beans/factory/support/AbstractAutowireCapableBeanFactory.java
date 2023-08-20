package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.context.ApplicationContextAware;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 继抽象类 AbstractBeanFactory 中 createBean 方法的实现。
 * 用于实现【创建对象】的具体功能，包含创建实例，属性注入，bean的后置处理。
 * 同时设置相应的实例化策略
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    // 依赖实例化策略
    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

//    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * 创建bean的具体实现
     * @param beanName
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;

        try {
            // newInstance()对于带入参的构造函数会报实例化异常。
//            bean = beanDefinition.getBeanClass().newInstance();
            // 【实例化】对象
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 实例化之后，给bean对象进行【属性填充】,将bean定义中的属性填充到bean对象中。
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 【bean的后置处理 beanPostProcessor】
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean field", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        registrySingleton(beanName, bean);
        return bean;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            // 这个方法为抽象类AbstractBeanFactory实现的接口ConfigurableBeanFactory中的方法，
            // 放在了抽象类AbstractBeanFactory的父类DefaultSingletonBeanRegistry中进行实现。

            // 注册到销毁对象池，对象池里存储的是适配器对象。
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {

        // 感知对象
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }

        }


        // 1. 执行 BeanPostProcessor 的 Before 处理
        Object wrappedBean = applyBeanPostProcessorBeforeInitialization(bean, beanName);

        // 2. 执行 Bean 对象的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init Method of bean[" + beanName + "] failed", e);
        }

        // 3. 执行 BeanPostProcessor 的 After 处理
        wrappedBean = applyBeanPostProcessorAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }


    /**
     * 初始化方法。
     * afterPropertiesSet对应实现接口的方式。
     * beanDefinition.getInitMethodName()对应xml文件配置的方式。
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws Exception
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1. 实现 InitializingBean 接口
        if (bean instanceof InitializingBean) {
            // 处理 afterPropertiesSet 方法
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 2. 配置信息 init-method { 判断是为了避免二次初始化 }
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (null == initMethod) {
                throw new BeansException("Could not find an init method named '" + initMethodName
                        + "' on bean with name '" + beanName + "'");
            }
            // 反射调用
            initMethod.invoke(bean);
        }
    }

    /**
     * 创建 bean 的实例。
     * @param beanDefinition
     * @param beanName
     * @param args
     * @return
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor constructorToUse = null;

        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors) {
            // 简单的查看形参数量是否一致
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        // 调用实例化策略进行实例的创建
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    /**
     * Bean 对象属性填充
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        try {
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {
                    // 例如 A 依赖 B，获取 B 的实例化对象
                    BeanReference beanReference = (BeanReference) value;
                    // 递归调用，使用的是 AbstractBeanFactory 中的getBean方法。
                    value = getBean(beanReference.getBeanName());
                }
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values:" + beanName, e);
        }
    }


    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor processor : getBeanPostProcessors ()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }
}
