package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.annotation.Autowired;
import cn.bugstack.springframework.beans.factory.annotation.Value;
import cn.bugstack.springframework.context.stereotype.Component;

import java.util.Random;


public class UserService implements IUserService {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;


    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "李帅，10001，合肥中行， " + token;
    }

    @Override
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }
}
