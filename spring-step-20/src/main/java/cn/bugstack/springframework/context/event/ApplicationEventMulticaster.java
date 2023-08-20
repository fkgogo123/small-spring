package cn.bugstack.springframework.context.event;

import cn.bugstack.springframework.context.ApplicationEvent;
import cn.bugstack.springframework.context.ApplicationListener;

/**
 * 事件广播器
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加监听器
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 删除监听器
     * @param listener
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播事件
     * @param event
     */
    void multicastEvent(ApplicationEvent event);

}
