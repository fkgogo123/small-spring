package cn.bugstack.springframework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory用于生成和使用对象的Bean工厂: bean的注册和获取
 */
public class BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public Object getBean(String name) {
        return beanDefinitionMap.get(name).getBean();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }
}
