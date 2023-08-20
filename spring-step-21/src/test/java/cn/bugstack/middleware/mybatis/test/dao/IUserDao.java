package cn.bugstack.middleware.mybatis.test.dao;

import cn.bugstack.middleware.mybatis.test.po.User;

import java.util.List;

public interface IUserDao {

    User queryUserInfoById(Long id);

    List<User> queryUserList(User user);

}
