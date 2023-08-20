package cn.bugstack.springframework.test.bean;

public class UserService {


    private String uId;

    private UserDao userDao;

    public UserService() {
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息:" + userDao.queryUserName(uId));
    }


}
