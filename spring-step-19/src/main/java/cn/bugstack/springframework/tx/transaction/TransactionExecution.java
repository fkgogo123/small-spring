package cn.bugstack.springframework.tx.transaction;

public interface TransactionExecution {

    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();

    boolean isCompleted();

}
