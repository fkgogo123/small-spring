实现功能：事务处理。通过AOP切面实现事务控制。

解析事务注解：SpringTransactionAnnotationParser
事务拦截处理：TransactionInterceptor、TransactionAspectSupport
事务管理操作：AbstractPlatformTransactionManager、DataSourceTransactionManager
事务同步管理器：TransactionSynchronizationManager

涉及到的两个链接：
用户的使用链接：JdbcTemplate使用的链接，执行connection.createStatement().execute()
切面链接：代理对象使用的链接，执行connection.commit, .rollback

用户使用的链接和AOP切面使用的链接要保持为同一个，这个如何解决？
执行事务时，事务管理器DataSourceTransactionManager
会将链接保存在事务同步管理器TransactionSynchronizationManager的ThreadLocal变量中。
ThreadLocal<Map<Object, Object>>，key为DataSource实例对象，value为Connection实例对象。


事务注解的原理?
实际上是对事务方法的AOP代理。
事务方法将作为被代理方法，事务自动提交的关闭，提交，回滚会交给事务拦截器进行执行。
事务的执行、事务的提交与回滚需要保持为同一个链接，这个由事务管理器来操作。
具体来说，事务管理器会将链接缓存在ThreadLocal中，JdbcTemplate在执行方法时会从该ThreadLocal中获取链接，从而保证了链接是同一个。

为什么用ThreadLocal？
便于多线程执行不同的事务，彼此不受影响。



