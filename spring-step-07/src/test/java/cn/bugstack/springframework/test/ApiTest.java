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
    public void test_BeanFactoryPostProcessor() {
        // 1. 初始化BeanFactory 接口
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件，注册bean对象
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. BeanDefinition 加载完成，在创建实例对象前，修改 BeanDefinition 的属性配置
        MyBeanFactoryPostProcessor factoryPostProcessor = new MyBeanFactoryPostProcessor();
        factoryPostProcessor.postProcessBeanFactory(beanFactory);

        // 4. 在对象实例化之后，修改Bean对象的属性信息，注册 BeanPostProcessor
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 5. 获取Bean对象的调用方法
        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

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
    }
}
