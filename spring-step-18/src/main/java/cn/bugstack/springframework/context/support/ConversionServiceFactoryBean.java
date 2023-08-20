package cn.bugstack.springframework.context.support;

import cn.bugstack.springframework.beans.factory.FactoryBean;
import cn.bugstack.springframework.beans.factory.InitializingBean;
import cn.bugstack.springframework.core.convert.ConversionService;
import cn.bugstack.springframework.core.convert.converter.Converter;
import cn.bugstack.springframework.core.convert.converter.ConverterFactory;
import cn.bugstack.springframework.core.convert.converter.GenericConverter;
import cn.bugstack.springframework.core.convert.support.DefaultConversionService;
import cn.bugstack.springframework.core.convert.support.GenericConversionService;
import com.sun.istack.internal.Nullable;

import java.util.Set;

/**
 * 提供创建 ConversionService 的工厂
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    @Nullable
    private Set<?> converters;

    @Nullable
    private GenericConversionService conversionService;


    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 组件服务对象的初始化。
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建 默认的转换服务
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, GenericConversionService registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter(((GenericConverter) converter)); // 注册转换处理器
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter(((Converter<?, ?>) converter)); // 注册转换处理器适配器
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter); // 注册转换处理器工厂适配器
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
