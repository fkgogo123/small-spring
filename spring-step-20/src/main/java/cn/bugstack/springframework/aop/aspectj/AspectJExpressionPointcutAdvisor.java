package cn.bugstack.springframework.aop.aspectj;

import cn.bugstack.springframework.aop.Pointcut;
import cn.bugstack.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 将切面Pointcut、拦截方法advice、和拦截表达式expression 包装在一起。
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 切面
    private AspectJExpressionPointcut pointcut;
    // 具体的拦截方法
    private Advice advice;
    // 表达式
    private String expression;

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            return new AspectJExpressionPointcut(expression);
        }
        return this.pointcut;
    }
}
