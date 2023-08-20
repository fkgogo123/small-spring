实现功能：通过注解注入属性信息

在实例化后，属性注入前，将注解中的值进行依赖注入，如果包含占位符，则一并处理。

xml文件读取时，在包扫描后，要注册一个BeanPostProcessor，即AutowiredAnnotationBeanPostProcessor，
用于对@Value注解和@Autowired注解的处理。


对于xml和注解中占位符的处理，都在PropertyPlaceholderConfigurer中，
它是一个BeanFactoryPostProcessor，在xml文件中需要进行显式定义，从而在容器中创建出PlaceholderConfigurer实例对象。

对于xml文件中的占位符：在实例化前，spring会遍历所有的工厂后置处理器，从而完成占位符的解析。
对于注解中的占位符：在实例化后，初始化前，进行处理，AutowiredAnnotationBeanPostProcessor。





