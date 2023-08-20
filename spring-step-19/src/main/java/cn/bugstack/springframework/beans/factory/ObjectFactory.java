package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
