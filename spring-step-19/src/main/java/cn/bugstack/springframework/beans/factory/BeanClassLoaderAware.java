package cn.bugstack.springframework.beans.factory;

/**
 * 类加载器感知接口
 *
 * 实现此接口，可以感知到所属的 ClassLoader 类加载器
 */
public interface BeanClassLoaderAware extends Aware {

    void setBeanClassLoader(ClassLoader classLoader);

}
