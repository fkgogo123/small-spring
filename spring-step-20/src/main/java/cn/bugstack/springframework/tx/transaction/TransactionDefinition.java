package cn.bugstack.springframework.tx.transaction;

import java.sql.Connection;

/**
 * 已存在事务加入，否则建新事务
 * 已存在事务加入，否则不使用事务
 *
 * 挂起已存在事务，建新事务
 * 挂起已存在事务，不使用事务
 *
 * 已存在事务则建嵌套事务，否则建新事务。
 *
 * 强制处于事务中，否则抛出异常
 * 强制不处于事务中
 */
public interface TransactionDefinition {

    /**
     * 这个是spring默认的事务传播行为
     * 如果正处于一个事务中，则加入，否则创建一个 新事务
     */
    int PROPAGATION_REQUIRED = 0;

    /**
     * 正处于一个事务中，则加入，否则不使用事务。
     */
    int PROPAGATION_SUPPORTS = 1;

    /**
     * mandatory: 强制性的
     * 如果正处于一个事务，则加入，否则抛出异常
     */
    int PROPAGATION_MANDATORY = 2;

    /**
     * 无论如何都会创建一个 新事务
     * 如果正处于一个事务中，则先挂起当前事务，然后创建
     */
    int PROPAGATION_REQUIRES_NEW = 3;

    /**
     * 不使用事务
     * 如果正处于一个事务中，则挂起当前事务，不使用
     */
    int PROPAGATION_NOT_SUPPORTED = 4;

    /**
     * 不使用事务，
     * 如果正处于一个事务中，则抛出异常
     */
    int PROPAGATION_NEVER = 5;

    /**
     * 嵌套事务
     * 如果正处于一个事务中，则创建一个事务嵌套其中（MySQL 采用 SAVEPOINT 保护点实现的），否则创建一个 新事务
     */
    int  PROPAGATION_NESTED = 6;

    /**
     * 使用默认的隔离级别
     */
    int ISOLATION_DEFAULT = -1;

    /**
     * 读未提交，读已提交，可重复读，串行化。
     */
    int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
    int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
    int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
    int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

    /**
     * 默认超时时间
     */
    int TIMEOUT_DEFAULT = -1;

    /**
     * 获取传播行为
     */
    int getPropagationBehavior();

    /**
     * 获取事务隔离级别
     */
    int getIsolationLevel();

    /**
     * 获取事务超时时间
     */
    int getTimeout();

    boolean isReadOnly();

    String getName();

}
