package cn.bugstack.springframework.core.convert.support;

import cn.bugstack.springframework.core.convert.ConversionService;
import cn.bugstack.springframework.core.convert.converter.Converter;
import cn.bugstack.springframework.core.convert.converter.ConverterFactory;
import cn.bugstack.springframework.core.convert.converter.ConverterRegistry;
import cn.bugstack.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class GenericConversionService implements ConversionService, ConverterRegistry {

    /**
     * 可用的转换处理器集合；
     * key：sourceType，targetType包装的类，（Class对象是单例的，重写了equals和hashcode方法）
     * value: 相应的 转换处理器 或 转换处理器工厂。（最终会通过适配器模式，实现统一的convert方法调用。）
     */
    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        GenericConverter converter = getConverter(sourceType, targetType);
        /**
         * 此处调用的convert方法实际上是适配器的convert方法。
         * 在ConverterAdapter的convert方法中，直接调用转换器的convert方法。
         * 在ConverterFactoryAdapter的convert方法中，会先getConverter获取转换器，再调用转换器的convert方法。
         */
        return ((T) converter.convert(source, sourceType, targetType));
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        // new了一个ConvertiblePair对象
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        for (GenericConverter.ConvertiblePair convertibleType : converterAdapter.getConvertibleTypes()) {
            // 转换处理器适配器
            converters.put(convertibleType, converterAdapter);
        }

    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (GenericConverter.ConvertiblePair convertibleType : converter.getConvertibleTypes()) {
            converters.put(convertibleType, converter);
        }

    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);
        for (GenericConverter.ConvertiblePair convertibleType : converterFactoryAdapter.getConvertibleTypes()) {
            // 工厂适配器
            converters.put(convertibleType, converterFactoryAdapter);
        }
    }

    /**
     * 给定一个converterFactory对象，获取其泛型参数，并包装成ConvertiblePair对象。
     *
     * @param object
     * @return
     */
    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object object) {
        Type[] types = object.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class sourceType = (Class) actualTypeArguments[0];
        Class targetType = (Class) actualTypeArguments[1];
        return new GenericConverter.ConvertiblePair(sourceType, targetType);
    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        // 获取Class对象及其所有父类的Class对象
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        // 查找是否存在相应的转换器。
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                // new出来的和set里存储的会相等吗?
                // 会的，ConvertiblePair重写了equals方法和hashcode方法，在get时会比较内容。
                GenericConverter converter = converters.get(convertiblePair);
                if (converter != null) {
                    return converter;
                }
            }
        }
        return null;
    }

    /**
     * 返回clazz类型及其所有父类类型的集合。
     *
     * @param clazz
     * @return
     */
    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = ((Converter<Object, Object>) converter);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            // 只包含特定对象的不可变集合
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }

    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = ((ConverterFactory<Object, Object>) converterFactory);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }
    }
}
