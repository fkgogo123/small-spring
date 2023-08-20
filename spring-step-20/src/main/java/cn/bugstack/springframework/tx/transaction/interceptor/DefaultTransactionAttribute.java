package cn.bugstack.springframework.tx.transaction.interceptor;

import cn.bugstack.springframework.tx.transaction.interceptor.TransactionAttribute;
import cn.bugstack.springframework.tx.transaction.support.DefaultTransactionDefinition;

public class DefaultTransactionAttribute extends DefaultTransactionDefinition implements TransactionAttribute {

    public DefaultTransactionAttribute(){
        super();
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        return (ex instanceof RuntimeException || ex instanceof Error);
    }

    @Override
    public String toString() {
        return "DefaultTransactionAttribute{}";
    }
}
