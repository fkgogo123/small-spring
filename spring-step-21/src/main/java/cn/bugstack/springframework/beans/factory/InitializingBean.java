package cn.bugstack.springframework.beans.factory;

public interface InitializingBean {

    /**
     * 在 Bean 对象属性填充后调用
     *
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
