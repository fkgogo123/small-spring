package cn.bugstack.springframework.core.convert.converter;

/**
 *
 * @param <S>   源数据类型
 * @param <R> 目标数据类型R。T instanceof R is true。也就是说R是实例对象T的类型或者T的直接或间接父类。
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}
