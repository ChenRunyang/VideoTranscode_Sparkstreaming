package com.cry.admin_reg.service;

import com.cry.admin_reg.Dao.UserDao;
import com.cry.admin_reg.model.Group;
import com.cry.admin_reg.model.User;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao){
        this.userDao=userDao;
    }

    public int add(String name,String password,String sex,String email,String phone){
        User user=new User();
        user.setName(name);
        user.setPassword(password);
        user.setSex(sex);
        user.setEmail(email);
        user.setPhone(phone);
        return userDao.insertUserName(user);
    }

    public User findByName(String name){
        return userDao.selectUser(name);
    }

    public User check(String name,String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return userDao.checkUser(user);
    }

    public int change(String name,String password,String email,String phone)
    {
        User user=new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        return userDao.change(user);
    }

    public List<String> selectGroup(String user)
    {
        int userid=Integer.parseInt(user);
        return userDao.selectGroup(userid);
    }

    public List<Group> selectGroupList(String user)
    {
        int userid = Integer.parseInt(user);
        return userDao.selectGroupList(userid);
    }

    public Group findGroupByname(String name,String username){
        int user = Integer.parseInt(username);
        return userDao.selectGroupByname(name,user);
    }

    public int insertGroup(String name,String user,String describe)
    {
        int userid = Integer.parseInt(user);
        return userDao.insertGroup(name,describe,userid);
    }

    public int deleteGroup(String name,String user)
    {
        int userid = Integer.parseInt(user);
        System.out.println(name);
        return userDao.deleteGroup(name,userid);
    }

}
