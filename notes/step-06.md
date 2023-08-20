主要完成bean的一些扩展接口，引入上下文类。

大致涉及两部分：
1.BeanDefinition的修改——BeanFactoryPostProcessor
2.Bean的修改——BeanPostProcessor
含两个方法：
- BeanPostProcessorBeforeInitialization
- BeanPostProcessorAfterInitialization

一个Bean的创建流程。

获取BeanDefinition
修改Definition
创建instance
属性注入
前置处理
初始化
后置处理


其中针对 BeanFactory 的扩展，在xml资源读取注册后，进行方法调用。
针对 Bean 的扩展，在创建 bean 的过程中，进行方法调用。


ApplicationContext是一个上下文接口
AbstractApplicationContext：定义整体流程。
【创建BeanFactory并加载xml文件 --> 执行FactoryPostProcessor --> 注册BeanPostProcessor --> 提前创建单例bean】
AbstractRefreshableApplicationContext：负责BeanFactory的创建
AbstractXmlApplicationContext：负责xml资源的加载
ClassPathXmlApplicationContext：负责启动整个流程，在构造函数中调用refresh。