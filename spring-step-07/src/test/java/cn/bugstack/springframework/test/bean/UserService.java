package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.DisposableBean;
import cn.bugstack.springframework.beans.factory.InitializingBean;

public class UserService implements InitializingBean, DisposableBean {


    private String uId;

    private String company;

    private String location;

    private UserDao userDao;

    public UserService() {
    }

    public String queryUserInfo() {
        return userDao.queryUserName(uId) + "," + company + "," + location;
    }


    /**
     * 方式二：实现 InitializingBean 和 DisposableBean 接口，并重写两个方法。
     *
     * 使用方式：直接调用方法
     */

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行：UserService.afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行：UserService.destroy");

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
