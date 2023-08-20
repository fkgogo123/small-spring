package cn.bugstack.springframework.beans.factory;

/**
 * beanName感知接口
 *
 * 实现此接口，可以感知到所属的 Bean 对象的名称
 */
public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}
