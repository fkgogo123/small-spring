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

/**
 * Aop 操作生成代理对象
 *
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    // 实现了BeanFactoryAware，该属性是在initializeBean方法中的aware判断处理过程中set的。
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((DefaultListableBeanFactory) beanFactory);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (isInfrastructureClass(beanClass)) return null;
        // 拿到所有的PointcutAdvisor类
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 和beanClass 匹配，判断是否需要创建代理类
            if (!classFilter.matches(beanClass)) continue;
            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = null;

            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 填充对应的属性信息
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor(((MethodInterceptor) advisor.getAdvice()));
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            // 使用 jdk 代理
            advisedSupport.setProxyTargetClass(false);
            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return null;
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

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
