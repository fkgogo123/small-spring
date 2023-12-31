package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.core.convert.converter.Converter;
import cn.bugstack.springframework.core.convert.support.StringToNumberConverterFactory;
import cn.bugstack.springframework.test.bean.Husband;
import cn.bugstack.springframework.test.converter.StringToIntegerConverter;
import cn.bugstack.springframework.test.converter.StringToLocalDateConverter;
import org.junit.Test;

import java.time.LocalDate;

public class ApiTest {

   @Test
   public void test_convert() {
      ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
      Husband husband = applicationContext.getBean("husband", Husband.class);
      System.out.println("测试结果：" + husband);
   }

   @Test
   public void test_StringToIntegerConverter() {
      StringToIntegerConverter converter = new StringToIntegerConverter();
      Integer num = converter.convert("1234");
      System.out.println("测试结果：" + num);
   }

   @Test
   public void testStringToNumberConverterFactory() {
      StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

      Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
      System.out.println("测试结果：" + stringToIntegerConverter.convert("1234"));

      Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
      System.out.println("测试结果：" + stringToLongConverter.convert("1234"));
   }

   @Test
   public void testStringToDateConverter() {
      StringToLocalDateConverter converter = new StringToLocalDateConverter("yyyy-MM-dd");
      LocalDate convert = converter.convert("2024-10-01");
      System.out.println("测试结果：" + converter.convert("2024-10-01"));

   }




}
