package com.cry.admin_reg.Dao;

import com.cry.admin_reg.model.Group;
import com.cry.admin_reg.model.User;
import org.apache.ibatis.annotations.*;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    @Select(value="select * from admin where name=#{name}")
    User selectUser(@Param("name") String name);

    @Select(value="SELECT name FROM `video`.`mygroup` where user=#{user}")
    List<String> selectGroup(@Param("user") int user);

    @Select(value = "select * from admin where name =#{name} and password =#{password}")
    User checkUser(User user);

    @Select(value ="SELECT * from `video`.`mygroup` where user=#{user}")
    List<Group> selectGroupList(@Param("user") int user);

    @Select(value = "SELECT * from `video`.`mygroup` where name=#{name} and user=#{user}")
    Group selectGroupByname(@Param("name") String name, @Param("user") int user);

    @Insert(value = "insert into admin (name, password, sex , email ,phone) values (#{name},#{password},#{sex},#{email},#{phone})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertUserName(User user);

    @Insert(value = "insert into `video`.mygroup (`name`,`describe`,`user`) values(#{name},#{describe},#{user})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertGroup(String name,String describe,int user);

    @Update(value = "update admin set password = #{password},email =#{email},phone = #{phone}")
    int change(User user);

    @Delete(value="delete from `video`.`mygroup` where user=#{user} and name=#{name}")
    int deleteGroup(String name,int user);
}
