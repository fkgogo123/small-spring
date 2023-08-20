package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.config.*;
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
            // 判断是否返回代理bean对象，转移到了初始化过程的后置处理中。
//            bean = resolveBeforeInstantiation(beanName, beanDefinition);
//            if (null != bean) {
//                return bean;
//            }

            // newInstance()对于带入参的构造函数会报实例化异常。
//            bean = beanDefinition.getBeanClass().newInstance();
            // 【实例化】对象
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 处理【注解】中的值、对象、占位符。在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            // 实例化之后，给bean对象进行【属性填充】,将bean定义中的属性填充到bean对象中。
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 【初始化】bean的后置处理 beanPostProcessor。
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean field", e);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 判断 是单例还是原型。单例才会进行注册，多例的从容器中获取不到，会进行新的创建，
        if (beanDefinition.isSingleton()) {
            registrySingleton(beanName, bean);
        }

        return bean;
    }

    /**
     * 在设置 Bean 对象的属性之前，允许 BeanPostProcessor 接口修改属性值
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                // 目前pvs没有得到相应的修改，由于bean已经实例化，注解中的值可以直接进行属性注入。
                if (null != pvs) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        // 判断是否需要代理，并获取代理对象
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (null != bean) {
            // 不理解，但是目前该方法无操作，只是返回bean。
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }


    protected Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (null != result) return result;
            }
        }
        return null;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

        // 非单例的bean对象不需要执行销毁方法。
        if (!beanDefinition.isSingleton()) return;

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
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 2. 执行 Bean 对象的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init Method of bean[" + beanName + "] failed", e);
        }

        // 3. 执行 BeanPostProcessor 的 After 处理
        // 代理对象的创建放在该步骤
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
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
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
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
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
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
