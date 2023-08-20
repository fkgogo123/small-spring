package cn.bugstack.springframework.test;

import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.core.io.DefaultResourceLoader;
import cn.bugstack.springframework.core.io.Resource;
import cn.bugstack.springframework.test.bean.UserDao;
import cn.bugstack.springframework.test.bean.UserService;
import cn.bugstack.springframework.test.common.MyBeanFactoryPostProcessor;
import cn.bugstack.springframework.test.common.MyBeanPostProcessor;
import cn.hutool.core.io.IoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class ApiTest {

    @Test
    public void test_xml() {
        // 1. 初始化 BeanFactory 接口
        // 包装了 BeanFactory的创建，BeanFactoryPostProcessor 和 BeanPostProcessor的扩展。
        // 改进之处在于，不用手动的去配置一些扩展。扩功能时，只需要实现相应的接口，上下文类能够完成自动配置。
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 注册钩子
        applicationContext.registerShutDownHook();

        // 2. 获取Bean对象的调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);

        System.out.println("ApplicationContextAware: " + userService.getApplicationContext());
        System.out.println("BeanFactoryAware: " + userService.getBeanFactory());
    }

    @Test
    public void test_15() {
        System.out.println(new Object());
        System.out.println(new Object());
    }
}
