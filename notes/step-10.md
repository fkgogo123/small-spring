实现功能：容器事件和事件监听器。观察者模式。

介绍：
ApplicationEvent是一个顶层的事件定义抽象类。
ApplicationContextEvent是直接子类，用于定义一般性的事件。
ContextClosedEvent、ContextRefreshedEvent是ApplicationContextEvent的直接子类，用于定义容器刷新和容器关闭的事件。

使用：
自定义事件类，继承ApplicationContextEvent或其它event类。
定义监听器类，继承ApplicationListener，并指定泛型，即需要接收处理的事件类型，
并编写收到消息后的处理逻辑。

内部流程：
容器启动refresh过程中，会初始化发布者，注册监听器。
发送消息的方法中，会进行事件广播multicastEvent，随后将已注册的监听器与event.class进行匹配，
获取需要接收消息的监听器，并调用监听器的onApplicationEvent方法。



