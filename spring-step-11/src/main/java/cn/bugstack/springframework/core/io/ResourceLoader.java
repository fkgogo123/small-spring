package cn.bugstack.springframework.core.io;

/**
 * 包资源加载器
 *
 * 依赖三种来源（classpath，file，url）的资源加载器
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);

}
