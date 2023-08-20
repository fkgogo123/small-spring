package cn.bugstack.springframework.aop;

public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
