package cn.bugstack.springframework.context;

import cn.bugstack.springframework.beans.factory.ListableBeanFactory;

/**
 * 上下文接口
 * 具有自动识别、资源加载、容器事件和事件监听器等功能。
 *
 * 继承 ListableBeanFactory 接口，ListableBeanFactory 接口继承了 BeanFactory 接口。
 * 继承了BeanFactory的getBean方法，并添加了根据名字获取容器中的实例，以及获取容器中所有bean的名字的方法。
 */
public interface ApplicationContext extends ListableBeanFactory {

}
