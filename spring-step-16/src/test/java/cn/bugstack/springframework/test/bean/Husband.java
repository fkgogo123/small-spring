package cn.bugstack.springframework.test.bean;

import java.lang.reflect.WildcardType;

public class Husband {

    private Wife wife;

    public String queryWife() {
        return "Husband.wife";
    }
}
