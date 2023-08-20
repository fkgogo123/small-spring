package cn.bugstack.springframework.test.converter;

import cn.bugstack.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter DATA_TIME_FORMATTER;

    public StringToLocalDateConverter(String pattern) {
        this.DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DATA_TIME_FORMATTER);
    }
}
