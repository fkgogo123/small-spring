package cn.bugstack.springframework.aop.framework;

import cn.bugstack.springframework.aop.AdvisedSupport;

/**
 * 代理工厂，根据不同的需求（advisedSupport.isProxyTargetClass()）,创建不同的代理对象。
 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
