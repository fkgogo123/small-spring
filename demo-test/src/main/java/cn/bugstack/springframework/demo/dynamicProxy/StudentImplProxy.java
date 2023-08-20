package cn.bugstack.springframework.demo.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StudentImplProxy implements InvocationHandler {

    private Object student;

    public void setStudent(Object student) {
        this.student = student;
    }

    public Object getDynamicProxyObject() {
        return Proxy.newProxyInstance(student.getClass().getClassLoader(), student.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if ("queryStudentName".equals(method.getName())) {
            System.out.println("你被代理了");
            result = method.invoke(student, args[0]);
        }
        return result;
    }
}
