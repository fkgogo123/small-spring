package cn.bugstack.springframework.beans.factory.config;

public class BeanDefinition {

    // 使用Class 可以将类的实例化放在容器中getBean时处理
    public Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
