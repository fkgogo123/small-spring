package cn.bugstack.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();

    /**
     * 方式一：xml配置init-method以及destroy-method方法的名字，并在对象中定义两个方法。
     *
     * 使用方式：反射执行。
     */

    public void initDataMethod() {
        System.out.println("执行：init-method");
        hashMap.put("10001", "高叔");
        hashMap.put("10002", "李帅");
        hashMap.put("10003", "ga伟");
    }

    public void destroyDataMethod() {
        System.out.println("执行：destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
