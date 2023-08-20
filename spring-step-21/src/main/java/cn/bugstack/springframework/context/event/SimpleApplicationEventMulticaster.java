package cn.bugstack.springframework.context.event;

import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.context.ApplicationEvent;
import cn.bugstack.springframework.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    /**
     * 事件广播
     * @param event
     */
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : getApplicationListeners(event)) {
            // 监听器执行处理方法
            listener.onApplicationEvent(event);
        }
    }
}
