package cn.bugstack.springframework.demo.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StudentTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StudentImpl student = new StudentImpl();
        Method method = Student.class.getMethod("sayHello", String.class);
        // 使用 反射 执行方法
        Object o = method.invoke(student, "李帅");
    }
}
