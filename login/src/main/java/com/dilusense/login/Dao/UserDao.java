package com.dilusense.login.Dao;

import com.dilusense.login.model.Group;
import com.dilusense.login.model.ModelInfo;
import com.dilusense.login.model.User;
import com.dilusense.login.model.VideoInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    @Select(value="select * from `virtualMan`.`admin` where name=#{name}")
    User selectUser(@Param("name") String name);

    @Select(value="SELECT name FROM `virtualMan`.`mygroup` where user=#{user}")
    List<String> selectGroup(@Param("user") int user);

    @Select(value = "select * from `virtualMan`.`admin` where name =#{name} and password =#{password}")
    User checkUser(User user);

    @Select(value ="SELECT * from `virtualMan`.`mygroup` where user=#{user}")
    List<Group> selectGroupList(@Param("user") int user);

    @Select(value = "SELECT * from `virtualMan`.`mygroup` where name=#{name} and user=#{user}")
    Group selectGroupByname(@Param("name") String name, @Param("user") int user);

    @Select(value = "SELECT * FROM `virtualMan`.`modelinfo` where user=#{userid}")
    List<ModelInfo> getmodelinfo(@Param("userid") int userid);

    @Select(value = "SELECT * FROM `virtualMan`.`modelinfo` where modelname=#{modelname} and user=#{user}")
    List<ModelInfo> getmodelinfobyName(@Param("modelname") String modelname,@Param("user") int user);

    @Select(value = "SELECT * FROM `virtualMan`.`videoinfo` where name=#{name} and user=#{user}")
    List<VideoInfo> getaudioinfobyName(@Param("name") String name,@Param("user") int user);

    @Select(value = "SELECT * FROM `virtualMan`.`videoinfo` where user=#{user}")
    List<VideoInfo> getvideoinfobyUser(@Param("user") int user);

    @Select(value = "SELECT COUNT(*) FROM `virtualMan`.`modelinfo` where state=0")
    int getBusymodel();

    @Select(value ="SELECT COUNT(*) FROM `virtualMan`.`videoinfo` where state=0")
    int getBusyvideo();

    @Insert(value = "insert into `virtualMan`.admin (name, password, sex , email ,phone) values (#{name},#{password},#{sex},#{email},#{phone})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertUserName(User user);

    @Insert(value = "insert into `virtualMan`.modelinfo(`name`, `user`, `group`, `videopath`, `state`, `modelpath`,`modelname`) values (#{name},#{user},#{group},#{videopath},#{state},#{modelpath},#{modelname})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertmodel(String name,int user,int group,String videopath,int state,String modelpath,String modelname);

    @Insert(value = "insert into `virtualMan`.videoinfo(`name`, `user`, `group`,`model`,`videopath`,`state`,`serverpath`) values (#{name},#{user},#{group},#{model},#{videopath},#{state},#{serverpath})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertvideo(String name,int user,int group,int model,String videopath,int state,String serverpath);

    @Insert(value = "insert into `virtualMan`.mygroup (`name`,`describe`,`user`) values(#{name},#{describe},#{user})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertGroup(String name,String describe,int user);

    @Update(value = "update `virtualMan`.`admin` set password = #{password},email =#{email},phone = #{phone}")
    int change(User user);

    @Update(value = "update `virtualMan`.`modelinfo` set state = #{state} where name =#{name} and user=#{user}")
    int updateModelstate(int user,String name,int state);

    @Update(value="update `virtualMan`.`modelinfo` set modelpath=#{modelpath} where name=#{name} and user=#{user}")
    int  updateModelpath(String modelpath,int user,String name);

    @Update(value ="update `virtualMan`.`videoinfo` set state = #{state} where name =#{name} and user=#{user}")
    int updateVideostate(int user,String name,int state);

    @Update(value = "update `virtualMan`.`videoinfo` set videopath=#{videopath} where name=#{name} and user=#{user}")
    int updateVideopath(String videopath,int user,String name);

    @Update(value = "update `virtualMan`.`videoinfo` set serverpath=#{serverpath} where name =#{name} and user=#{user}")
    int updateServerpath(String serverpath,int user,String name);

    @Delete(value = "delete from `virtualMan`.`videoinfo` where name =#{name} and user=#{user}")
    int deleteVideo(int user,String name);

    @Delete(value="delete from `virtualMan`.`mygroup` where user=#{user} and name=#{name}")
    int deleteGroup(String name,int user);
}
