package cn.bugstack.springframework.test;

import cn.bugstack.springframework.aop.AdviseSupport;
import cn.bugstack.springframework.aop.TargetSource;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.bugstack.springframework.aop.framework.Cglib2AopProxy;
import cn.bugstack.springframework.aop.framework.JdkDynamicAopProxy;
import cn.bugstack.springframework.test.bean.IUserService;
import cn.bugstack.springframework.test.bean.UserService;
import cn.bugstack.springframework.test.bean.UserServiceInterceptor;
import org.junit.Test;

import java.lang.reflect.Method;

public class ApiTest {

   @Test
    public void test_aop() throws NoSuchMethodException {
       AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.UserService.*(..))");
       Class<UserService> clazz = UserService.class;
       Method method = clazz.getDeclaredMethod("queryUserInfo");

       System.out.println(pointcut.matches(clazz));
       System.out.println(pointcut.matches(method, clazz));
   }

   @Test
   public void test_dynamic() {
      // 目标对象
      IUserService userService = new UserService();
      // 组装代理信息
      AdviseSupport adviseSupport = new AdviseSupport();
      adviseSupport.setTargetSource(new TargetSource(userService));
      adviseSupport.setMethodInterceptor(new UserServiceInterceptor());
      adviseSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.IUserService.*(..))"));

      // 代理对象
      IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(adviseSupport).getProxy();

      System.out.println("测试结果：" + proxy_jdk.queryUserInfo());

      IUserService proxy_cglib = (IUserService) new Cglib2AopProxy(adviseSupport).getProxy();
      System.out.println("测试结果：" + proxy_cglib.register("花花"));


   }

}
