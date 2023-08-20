package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.DisposableBean;
import cn.bugstack.springframework.beans.factory.ObjectFactory;
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

    protected static final Object NULL_OBJECT = new Object();

    // 一级缓存
    private Map<String, Object> singletonObjects = new HashMap<>();

    // 二级缓存
    protected final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();

    // 三级缓存
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    // 存储的是 （beanName, 相应的适配器对象）
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        // 查询一级缓存
        Object singletonObject = singletonObjects.get(beanName);
        if (null == singletonObject) {
            // 查询二级缓存
            singletonObject = earlySingletonObjects.get(beanName);
            if (null == singletonObject) {
                // 查询三级缓存
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    // 从三级缓存中获取aop代理对象
                    singletonObject = singletonFactory.getObject();
                    // 添加到二级缓存中，同时删除三级缓存。
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registrySingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            earlySingletonObjects.remove(beanName);
        }

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
