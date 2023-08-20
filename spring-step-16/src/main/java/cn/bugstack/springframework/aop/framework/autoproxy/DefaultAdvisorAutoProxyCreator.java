package cn.bugstack.springframework.aop.framework.autoproxy;

import cn.bugstack.springframework.aop.*;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.bugstack.springframework.aop.framework.ProxyFactory;
import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.BeanFactoryAware;
import cn.bugstack.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Aop 操作生成代理对象
 *
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    // 实现了BeanFactoryAware，该属性是在initializeBean方法中的aware判断处理过程中set的。
    private DefaultListableBeanFactory beanFactory;

    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((DefaultListableBeanFactory) beanFactory);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 正常的AOP代理
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    /**
     * 三级缓存的AOP代理
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    /**
     * // 判断对象是否需要被代理，返回目标对象 or 代理对象
     *
     * @param bean
     * @param beanName
     * @return
     */
    protected Object wrapIfNecessary(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (isInfrastructureClass(beanClass)) return bean;
        // 拿到所有的PointcutAdvisor类
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 和beanClass 匹配，判断是否需要创建代理类
            if (!classFilter.matches(beanClass)) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(bean);
            // 填充对应的属性信息
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor(((MethodInterceptor) advisor.getAdvice()));
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            // 使用 cglib 代理
            advisedSupport.setProxyTargetClass(true);
            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }
}
