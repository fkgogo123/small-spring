package cn.bugstack.springframework.tx.transaction.interceptor;

import cn.bugstack.springframework.tx.transaction.TransactionDefinition;

public interface TransactionAttribute extends TransactionDefinition {

    boolean rollbackOn(Throwable ex);

}
