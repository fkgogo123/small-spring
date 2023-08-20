package cn.bugstack.springframework.core.convert.converter;

/**
 * 类型转换处理器
 *
 * @param <S>   源数据类型
 * @param <T> 目标数据类型
 */
public interface Converter<S, T> {

    T convert(S source);

}
