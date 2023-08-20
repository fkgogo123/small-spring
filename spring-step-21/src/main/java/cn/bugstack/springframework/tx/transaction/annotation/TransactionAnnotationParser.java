package cn.bugstack.springframework.tx.transaction.annotation;

import cn.bugstack.springframework.tx.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

/**
 * 用于解析已知事务注解类型的 策略 接口
 */
public interface TransactionAnnotationParser {

    /**
     * 解析事务注解的属性，并包装成 TransactionAttribute 对象。
     * @param element
     * @return
     */
    TransactionAttribute parseTransactionAnnotation(AnnotatedElement element);
}
