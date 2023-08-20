
主要问题，实现对象相关test
定义信息的xml文件解析，注册。

定义Resource接口以及对三种来源（classpath，file，url）的资源加载器。

定义BeanDefinitionReader接口，完成bean定义的读取和注册。
该流程需要依赖ResourceLoader和beanDefinition容器。

先将读取出来的配置信息创建成BeanDefinition以及PropertyValues，再将完整的bean定义注册到bean容器中。

已经初步实现 <b>在配置文件中解析并注册 Bean 的信息</b> ，再<b>通过 Bean 工厂获取 Bean 对象</b>。


AbstractBeanFactory: 定义顶层流程,一个getBean方法。
AbstractAutowireCapableBeanFactory：负责流程中 Bean实例的创建 和 属性的注入
DefaultListableBeanFactory：负责流程中 BeanDefinition的获取

XmlBeanDefinitionReader：负责 xml资源的扫描与BeanDefinition的注册。
目前仍然是手动调用进行加载，后续可以使用上下文类进行自动调用。
