package cn.bugstack.springframework.core.convert.converter;

/**
 * 类型转换器 和 类型转换工厂 注册接口
 *
 */
public interface ConverterRegistry {

    /**
     * 添加一个普通的转换处理器
     *
     * @param converter
     */
    void addConverter(Converter<?, ?> converter);

    /**
     * 添加一个通用的转换处理器
     *
     * @param converter
     */
    void addConverter(GenericConverter converter);

    /**
     * 添加一个类型转换工厂
     *
     * @param converterFactory
     */
    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

}
