package cn.bugstack.springframework.demo.dynamicProxy;

import cn.hutool.core.lang.hash.Hash;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class StudentProxy {

    public Student getDynamicProxyObject() {

        InvocationHandler handler = (proxy, method, args) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("001", "张三");
            map.put("002", "李四");
            map.put("003", "王五");
            return "你被代理了" + method.getName() + ": " + map.get(args[0].toString());
        };

        return ((Student) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{Student.class}, handler));
    }
}
