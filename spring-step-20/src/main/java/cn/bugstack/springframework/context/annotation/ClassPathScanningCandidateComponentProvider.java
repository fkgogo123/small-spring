package cn.bugstack.springframework.context.annotation;


import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.stereotype.Component;
import cn.hutool.core.util.ClassUtil;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫描所有使用了 Component 注解的类
 *
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 扫描所有使用Component注解的Bean对象
     *
     * @param basePackage
     * @return
     */
    public Set<BeanDefinition> findCandidateComponent(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);

        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;

    }

}
