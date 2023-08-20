package cn.bugstack.springframework.demo.dynamicProxy;

import java.util.HashMap;

public class StudentImpl implements Student {

    @Override
    public String queryStudentName(String stuId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("001", "张三");
        map.put("002", "李四");
        map.put("003", "王五");
        return map.get(stuId);
    }
}
