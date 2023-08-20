实现功能：自动扫描注册Bean对象.

支持xml文件配置basePackages，进行包扫描，得到所有标注Component注解的类，且beanName支持注解定义。
支持xml文件中属性的占位符配置。读取属性配置文件，并将beanDefinition中相应的属性进行替换。

ClassPathScanningCandidateComponentProvider：扫描所有使用了 Component 注解的类
ClassPathBeanDefinitionScanner：具体处理包扫描, 注册BeanDefinition
以上两个方法会在xml文件读取的时候调用。



bug：
包扫描的时候会注册BeanDefinition，若xml文件中也配置了相应的bean，则会进行二次注册。
包扫描的bean未进行属性填充。
