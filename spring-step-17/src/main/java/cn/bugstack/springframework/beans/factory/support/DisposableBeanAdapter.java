package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.DisposableBean;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        // 从 bean定义中拿到name
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    /**
     * 销毁方法。
     * bean.destroy()对应实现接口的方式。
     * beanDefinition.getDestroyMethodName()对应 xml文件配置的方式。
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        // 1. 实现 DisposableBean 接口
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        // 2. 配置 destroy-method { 判断是为了避免二次销毁 }
        if (StrUtil.isNotEmpty(destroyMethodName) &&
                !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (null == destroyMethod) {
                throw new BeansException("Could not find a destroy method named '" + destroyMethodName
                        + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }

    }
}
