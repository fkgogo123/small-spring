package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 切面类
 */

public class SpouseAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object target) throws Throwable {
        System.out.println("关怀小俩口（切面）：" + method);
    }
}
