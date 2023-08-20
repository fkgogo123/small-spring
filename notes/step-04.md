AbstractBeanFactory抽象类规范了一个getBean的流程。即getBeanDefinition 和 createBean。
流程的具体实现放在该抽象类的子类中实现。

- 第一个问题，解决无参构造器和带参构造器的实例化。</br>
在createBean方法中，依赖两种实例化策略来创建对象，引入流程createBeanInstance。
- 第二个问题，解决属性注入的问题。
属性注入在实例化之后，因此在createBean方法中，引入流程，applyPropertyValues。
使用PropertyValue类和PropertyValues集合，遍历，递归，注入属性。
属性为对象时，使用BeanReference进行标识。