实现功能：区分单例与多例作用域，实现FactoryBean 并 实现最基本的动态代理，这也是ORM的雏形。

使用时：创建一个ProxyBeanFactory，并实现了IUserDao接口。
在代理类的getObject方法中，会提供一个InvocationHandler代理对象，这个代理对象会存储在factoryBeanObjectCache中，
当bean是一个factoryBean（实现了该接口）时，会进行代理对象的创建和缓存。
调用接口方法时，会执行代理对象中的handler处理逻辑。

createBean之后，如果是一个factoryBean，则会注册相应的代理对象。

针对单例的bean，在容器启动时会进行实例化，并在 singletonObjects 和 factoryBeanObjectCache进行注册。
在后续getBean时，针对单例的bean获取的是从容器中得到的共享实例，针对多例的会新建实例。


关于对象代理：

一种是静态代理：
1.创建一个接口（核心功能）
2.创建实现类（实现接口的方法）
3.创建代理类（实现接口的方法还可添加自己的方法）

一种是动态代理：
1.创建接口，定义目标类要完成的功能
2.创建目标类实现接口
3.创建InvocationHandler接口实现类，在invoke方法中完成代理类的功能
4.使用Proxy类的静态方法，创建代理对象，并把返回值转为接口类型。
Proxy.newProxyInstance(student.getClass().getClassLoader(), student.getClass().getInterfaces(),
this);

或者
1.创建接口，定义目标类要完成的功能
2.创建代理类，将代理类功能写在hander中，并返回代理对象。
3.使用代理类调用接口方法。
((Student) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
new Class[]{Student.class}, handler));

相同之处：都需要拿到代理对象，并调用接口方法。
不同之处：一个需要目标实现类，一个不需要目标实现类。