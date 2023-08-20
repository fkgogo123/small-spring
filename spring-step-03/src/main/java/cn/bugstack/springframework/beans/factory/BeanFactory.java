package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    // 带有入参的构造函数，将入参传入getBean()中，然后到createBean方法中
    Object getBean(String name, Object... args) throws BeansException;
}
