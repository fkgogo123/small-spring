package cn.bugstack.springframework.context;

import cn.bugstack.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     *
     * 执行该方法时，会获取 BeanDefinition，并将一些 postProcessor 进行注册。
     * 并对单例 Bean 进行实例化。
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 定义 注册虚拟机钩子 的方法
     */
    void registerShutDownHook();

    // 手动执行关闭
    void close();

}
