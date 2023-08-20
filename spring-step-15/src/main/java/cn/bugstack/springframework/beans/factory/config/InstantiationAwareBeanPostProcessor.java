package cn.bugstack.springframework.beans.factory.config;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValues;

/**
 * 通过定义接口的方式来区分不同的扩展点。
 *
 * 在 bean 的生命周期中，通过 instanceof 方法来从众多beanPostProcessors中筛选出相应扩展点的 bean后置处理器。
 *
 * （服务于spring框架对扩展点后置处理器的 感知）
 *
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法。前置通知。
     *
     * 实例化之前。
     * 用于创建aop的代理对象
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     *
     * 用于对注解中的属性注入。
     *
     * 实例化之后，属性注入之前。
     * 需要处理注解中的 （值、占位符、对象）。
     *
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;



}
