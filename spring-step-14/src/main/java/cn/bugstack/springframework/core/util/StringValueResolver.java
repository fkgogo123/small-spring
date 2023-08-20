package cn.bugstack.springframework.core.util;

/**
 * 解析字符串操作的接口
 *
 * 如：@Value("${token}")
 *
 */
public interface StringValueResolver {

    String resolveStringValue(String strVal);

}
