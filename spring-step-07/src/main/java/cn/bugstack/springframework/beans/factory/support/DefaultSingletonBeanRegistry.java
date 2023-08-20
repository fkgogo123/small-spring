package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.DisposableBean;
import cn.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实现注册和获取单例对象的接口
 *
 * 添加了实例销毁池和销毁实例注册方法，以及销毁方法
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();

    // 存储的是 （beanName, 相应的适配器对象）
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registrySingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableNames = keySet.toArray();

        for (int i = disposableNames.length - 1; i >= 0; i--) {
            Object beanName = disposableNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                // 调用适配器的destroy方法。该方法中针对接口和配置两种方式都进行了处理。
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }

        }
    }
}
