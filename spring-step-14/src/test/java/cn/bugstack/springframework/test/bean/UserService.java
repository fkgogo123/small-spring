package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.annotation.Autowired;
import cn.bugstack.springframework.beans.factory.annotation.Value;
import cn.bugstack.springframework.context.stereotype.Component;

import java.util.Random;

@Component("userService")
public class UserService implements IUserService {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Value("${token}")
    private String token;

    @Autowired
    private UserDao userDao;

    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userDao.queryUserName("10001") + ", " + token;
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
