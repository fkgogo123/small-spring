package cn.bugstack.springframework.tx.transaction.interceptor;

import cn.bugstack.springframework.tx.transaction.PlatformTransactionManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

/**
 * 事务拦截处理
 */
public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor, Serializable {

    public TransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource transactionAttributeSource) {
        setTransactionManager(ptm);
        setTransactionAttributeSource(transactionAttributeSource);
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 调用切面支撑类（父类）的事务处理方法。
        return invokeWithinTransaction(invocation.getMethod(), invocation.getThis().getClass(), invocation::proceed);
    }
}
