package com.cry.videotransform.service;

import com.cry.videotransform.Dao.VideoDao;
import com.cry.videotransform.model.RoomInfo;
import com.cry.videotransform.model.VideoInfo;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoService {
    @Resource
    private VideoDao videoDao;

    @Autowired
    public VideoService(VideoDao videoDao){
        this.videoDao=videoDao;
    }

    public int getgroupid(int userid,String groupname){
        return videoDao.getgroupid(userid,groupname);
    }

    public String getgroupname(int id){ return videoDao.getgroupname(id);}

    public int insertVideo(String name,String videopath,int videotime,String groupname,String sendtime,String admin,int width,int height){
        int userid=Integer.parseInt(admin);
        int groupid=getgroupid(userid,groupname);
        System.out.println(groupid);
        return videoDao.insertvideo(name,videopath,videotime,groupid,sendtime,userid,width,height);
    }

    public int delvideo(String user,String name)
    {
        int userid = Integer.parseInt(user);
        return videoDao.delvideo(userid,name);
    }

    public List<VideoInfo> getVideoInfo(String user)
    {
        int userid=Integer.parseInt(user);
        return videoDao.getvideoinfo(userid);
    }

    public String getvideopath(String user,String name)
    {
        int userid = Integer.parseInt(user);
        return videoDao.getvideopath(userid,name);
    }

    public int insertroom(String user, String name, String password)
    {
        int userid = Integer.parseInt(user);
        String path="src/main/resources/static/"+user+"/live/M3U8List/"+name+".m3u8";
        return videoDao.insertroom(userid,password,path,"正在直播");
    }

    public List<RoomInfo> checkstate(String user)
    {
        int userid=Integer.parseInt(user);
        return videoDao.checkstate(userid,"正在直播");
    }

    public String getliveroom(String user,String password)
    {
        int userid = Integer.parseInt(user);
        return videoDao.getliveroom(userid,password);
    }

    public int delroom(String room)
    {
        int roomid = Integer.parseInt(room);
        return videoDao.delroom(roomid);
    }

}
