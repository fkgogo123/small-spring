实现功能：感知（Aware）容器对象。

Aware是一个感知标记性接口，只起到标记的作用，用于获取出属于此类的实现类。

为什么要在创建对象的时候去感知对象，有什么功能和作用？
目的：使得用户可以访问到bean的一些上下文对象。
使用方法：自定义对象类并实现Aware接口，重写一些set方法。得到最终的实例对象后，可以访问其beanName，classLoader等
实现方法：在BeanPostProcessor中，aware的set方法会在bean创建的时候进行调用并进行属性注入，

其他重点：
为什么applicationContext的设置要添加到一个BeanPostProcessor中？
原因：在创建bean的时候时无法获取上下文对象的，而通过注册beanPostProcessor可以将上下文对象注入到bean的属性中。
具体来说，在上下文对象调用refresh方法时，beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
上下文在此处进行了传递，最终注入到bean的属性中。






