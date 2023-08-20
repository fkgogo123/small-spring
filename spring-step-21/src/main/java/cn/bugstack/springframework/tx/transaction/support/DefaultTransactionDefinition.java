package cn.bugstack.springframework.tx.transaction.support;

import cn.bugstack.springframework.tx.transaction.TransactionDefinition;

import java.io.Serializable;

public class DefaultTransactionDefinition implements TransactionDefinition, Serializable {

    private int propagationBehavior = PROPAGATION_REQUIRED;

    private int isolationLevel = ISOLATION_DEFAULT;

    private int timeout = TIMEOUT_DEFAULT;

    private boolean readOnly = false;

    private String name;

    public DefaultTransactionDefinition() {
    }

    public DefaultTransactionDefinition(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    public DefaultTransactionDefinition(TransactionDefinition other) {
        this.propagationBehavior = other.getPropagationBehavior();
        this.isolationLevel = other.getIsolationLevel();
        this.timeout = other.getTimeout();
        this.readOnly = other.isReadOnly();
        this.name = other.getName();
    }

    public void setPropagationBehavior(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    public void setIsolationLevel(int isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    @Override
    public int getPropagationBehavior() {
        return this.propagationBehavior;
    }

    @Override
    public int getIsolationLevel() {
        return this.isolationLevel;
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
