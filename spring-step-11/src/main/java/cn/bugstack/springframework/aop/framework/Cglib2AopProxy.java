package cn.bugstack.springframework.aop.framework;

import cn.bugstack.springframework.aop.AdviseSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy {

    private final AdviseSupport advised;

    public Cglib2AopProxy(AdviseSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        // 返回this.target.getClass().getInterfaces();
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdviseSupport advised;

        public DynamicAdvisedInterceptor(AdviseSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();

        }

        private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

            private final MethodProxy methodProxy;

            public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
                super(target, method, arguments);
                this.methodProxy = methodProxy;
            }
        }
    }
}
