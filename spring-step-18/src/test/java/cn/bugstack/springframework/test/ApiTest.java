package cn.bugstack.springframework.test;

import cn.bugstack.springframework.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.jdbc.core.ColumnMapRowMapper;
import cn.bugstack.springframework.jdbc.core.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ApiTest {

   private JdbcTemplate jdbcTemplate;

   @Before
   public void init() {
      ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
      jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
   }

   @Test
   public void execute() {
      jdbcTemplate.execute("insert into user (id, userId, userHead, createTime, updateTime) values (2, '1280313', '01_50', now(), now())");

   }

   @Test
   public void queryForListTest() {
      List<Map<String, Object>> allResult = jdbcTemplate.queryForList("select * from user");
      for (Map<String, Object> objectMap : allResult) {
         System.out.println("测试结果：" + objectMap);
      }
   }

   @Test
   public void queryForListTest1() {
      List<Map<String, Object>> oneResult = jdbcTemplate.query("select * from user where id = 1", new ColumnMapRowMapper());
      for (Map<String, Object> objectMap : oneResult) {
         System.out.println("测试结果：" + objectMap);
      }
   }





}