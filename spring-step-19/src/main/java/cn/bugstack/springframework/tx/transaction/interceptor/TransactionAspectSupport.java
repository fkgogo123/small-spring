package cn.bugstack.springframework.tx.transaction.interceptor;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.BeanFactoryAware;
import cn.bugstack.springframework.beans.factory.InitializingBean;
import cn.bugstack.springframework.tx.transaction.PlatformTransactionManager;
import cn.bugstack.springframework.tx.transaction.TransactionStatus;
import cn.bugstack.springframework.util.ClassUtils;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 事务切面支撑类
 */
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {

    private static final ThreadLocal<TransactionInfo> transactionInfoHolder =
            new NamedThreadLocal<>("Current aspect-driven transaction");

    private BeanFactory beanFactory;

    private TransactionAttributeSource transactionAttributeSource;

    private PlatformTransactionManager transactionManager;

    /**
     * 事务处理。执行、提交/回滚
     *
     * @param method
     * @param targetClass
     * @param invocation
     * @return
     * @throws Throwable
     */
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocation) throws Throwable {

        TransactionAttributeSource tas = getTransactionAttributeSource();
        // 查找事务注解 Transactional
        TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);

        PlatformTransactionManager manager = determineTransactionManager();
        String joinPointIdentification = methodIdentification(method, targetClass);
        // 如果需要创建事务，则会暂存ThreadLocal中的旧事务信息，并替换为新事务信息
        TransactionInfo txInfo = createTransactionIfNecessary(manager, txAttr, joinPointIdentification);
        Object retVal = null;
        try {
            retVal = invocation.proceedWithInvocation(); // 执行事务方法
        } catch (Throwable e) {
            completeTransactionAfterThrowing(txInfo, e); // 提交 或 回滚 事务
            throw e;
        } finally {
            // 恢复ThreadLocal中旧的事务信息
            cleanupTransactionInfo(txInfo);
        }
        commitTransactionAfterReturning(txInfo);

        return retVal;
    }

    public TransactionAttributeSource getTransactionAttributeSource() {
        return transactionAttributeSource;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 当前使用DataSourceTransactionManager
     */
    protected PlatformTransactionManager determineTransactionManager() {
        return getTransactionManager();
    }

    /**
     * 获取目标方法的唯一标识
     */
    private String methodIdentification(Method method, Class<?> targetClass) {
        return ClassUtils.getQualifiedMethodName(method, targetClass);
    }

    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinPointIdentification) {
        if (txAttr != null && txAttr.getName() == null) {
            txAttr = new DelegatingTransactionAttribute(txAttr) {
                @Override
                public String getName() {
                    return joinPointIdentification;
                }
            };
        }

        TransactionStatus status = null;
        if (txAttr != null) {
            if (tm != null) {
                status = tm.getTransaction(txAttr);
            }
        }
        return prepareTransactionInfo(tm, txAttr, joinPointIdentification, status);
    }

    protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinPointIdentification, TransactionStatus status) {
        TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinPointIdentification);
        if (txAttr != null) {
            txInfo.newTransactionStatus(status);
        }
        txInfo.bindToThread();
        return txInfo;
    }

    protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
        if (null != txInfo && null != txInfo.getTransactionStatus()) {
            if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
                txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
            } else {
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            }
        }
    }

    protected void cleanupTransactionInfo(TransactionInfo txInfo) {
        if (null != txInfo) {
            txInfo.restoreThreadLocalStatus();
        }
    }

    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        if (null != txInfo && null != txInfo.getTransactionStatus()) {
            txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    protected interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    protected final class TransactionInfo {

        private final PlatformTransactionManager transactionManager;
        private final TransactionAttribute transactionAttribute;
        private final String joinPointIdentification;
        private TransactionStatus transactionStatus;
        private TransactionInfo oldTransactionInfo;

        public TransactionInfo(PlatformTransactionManager transactionManager,
                               TransactionAttribute transactionAttribute, String joinPointIdentification) {
            this.transactionManager = transactionManager;
            this.transactionAttribute = transactionAttribute;
            this.joinPointIdentification = joinPointIdentification;
        }

        public PlatformTransactionManager getTransactionManager() {
            Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");
            return transactionManager;
        }

        public TransactionAttribute getTransactionAttribute() {
            return transactionAttribute;
        }

        public String getJointPointIdentification() {
            return joinPointIdentification;
        }

        public TransactionStatus getTransactionStatus() {
            return transactionStatus;
        }

        public void newTransactionStatus(TransactionStatus status) {
            this.transactionStatus = status;
        }

        public boolean hasTransaction() {
            return null != this.transactionStatus;
        }

        private void bindToThread() {
            this.oldTransactionInfo = transactionInfoHolder.get();
            transactionInfoHolder.set(this);
        }

        private void restoreThreadLocalStatus() {
            transactionInfoHolder.set(this.oldTransactionInfo);
        }

    }

}
