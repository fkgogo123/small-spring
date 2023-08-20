实现功能：添加Bean的初始化和销毁方法，晚上Bean的生命周期。

Bean的准备过程：
spring容器创建，xml资源扫描，bean定义注册，bean定义修改，bean扩展注册。

Bean的生命周期：
获取bean定义，实例化，依赖注入，前置处理，<u>初始化(afterPropertiesSet)</u>，后置处理，使用，<u>销毁</u>。


支持两种实现方式：配置和接口。
配置：在xml文件中定义init-method和destroy-method属性，指定方法名字，在实例对象中定义相应的方法。
接口：实例对象 实现相应的接口，并重写 afterPropertiesSet 和 destroy 方法。

init-method方法的执行 在 invokeInitMethods 中进行了适配。
destroy方法的执行在 DisposableBeanAdapter 中进行了适配。


