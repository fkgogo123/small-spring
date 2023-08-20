package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.context.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();
    static {
        hashMap.put("10001", "高叔，上海，支付宝");
        hashMap.put("10002", "李帅，合肥，中行");
        hashMap.put("10003", "嘎伟，上海，天翼云");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}
