package com.cry.videotransform.Dao;

import com.cry.videotransform.model.RoomInfo;
import com.cry.videotransform.model.VideoInfo;
import org.apache.ibatis.annotations.*;
import org.bytedeco.javacv.FrameGrabber;

import java.util.List;

@Mapper
public interface VideoDao {
    @Select(value = "SELECT id FROM `video`.`mygroup` where name=#{name} and user=#{user}")
    int getgroupid(@Param("user") int user,@Param("name") String name);

    @Select(value = "SELECT name FROM `video`.`mygroup` where id=#{id}")
    String getgroupname(@Param("id") int id);

    @Select(value = "SELECT * FROM `video`.`videoinfo` where userid=#{userid}")
    List<VideoInfo> getvideoinfo(@Param("userid") int userid);

    @Select(value = "SELECT videopath FROM `video`.`videoinfo` where userid=#{userid} and name=#{name}")
    String getvideopath(@Param("userid") int userid,@Param("name") String name);

    @Select(value = "SELECT `roomid` FROM `video`.`room` where userid=#{userid} and state=#{state}")
    List<RoomInfo> checkstate(@Param("userid") int userid, @Param("state") String state);

    @Select(value = "SELECT `m3u8_path` FROM `video`.`room` where roomid=#{roomid} and password=#{password}")
    String getliveroom(@Param("roomid") int roomid,@Param("password") String password);

    @Insert(value = "insert into `video`.videoinfo (`name`,`videopath`,`videotime`,`groupid`,`sendtime`,`userid`,`width`,`height`) values(#{name},#{videopath},#{videotime},#{groupid},#{sendtime},#{userid},#{width},#{height})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertvideo(String name,String videopath,int videotime,int groupid,String sendtime,int userid,int width,int height);

    @Insert(value = "insert into `video`.room(`roomid`,`password`,`m3u8_path`,`state`) values(#{roomid},#{password},#{m3u8_path},#{state})")
    int insertroom(int roomid,String password,String m3u8_path,String state);

    @Delete(value = "delete from `video`.`videoinfo` where userid=#{userid} and name=#{name}")
    int delvideo(int userid,String name);

    @Delete(value = "delete from `video`.`room` where roomid = #{roomid}")
    int delroom(int roomid);
}
