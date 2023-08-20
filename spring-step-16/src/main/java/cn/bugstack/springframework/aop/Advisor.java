package cn.bugstack.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 访问者，通知者
 *
 * Pointcut功能和Advice功能的组合。
 *
 * Pointcut: 用于获取连接点JointPoint。
 * Advice：取决于JointPoint执行什么操作。
 */
public interface Advisor {

    /**
     * 获取切面的通知部分，可能是Interceptor，可能是before advice，也可能是throws advice。
     *
     * @return
     */
    Advice getAdvice();
}
