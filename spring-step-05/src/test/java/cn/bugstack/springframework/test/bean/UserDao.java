package cn.bugstack.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "高叔");
        hashMap.put("10002", "李帅");
        hashMap.put("10003", "ga伟");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
