package cn.bugstack.springframework.context;

import cn.bugstack.springframework.beans.factory.Aware;

/**
 * 感知所属的 ApplicationContext 应用上下文信息
 *
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}
