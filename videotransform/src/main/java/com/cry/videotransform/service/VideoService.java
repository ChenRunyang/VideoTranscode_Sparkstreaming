package com.cry.videotransform.service;

import com.cry.videotransform.Dao.VideoDao;
import com.cry.videotransform.model.RoomInfo;
import com.cry.videotransform.model.VideoInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideoService {

    private ArrayList<Pair<Date, Integer>> finishnum;
    private Integer tasknum;
    private ArrayList<Pair<Date, Integer>> failnum;

    @Resource
    private VideoDao videoDao;

    @Autowired
    public VideoService(VideoDao videoDao){
        this.videoDao=videoDao;
        if(this.getFinishnum()==null)    //实例化对象
        {
            this.setFinishnum(new ArrayList<>());
        }
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

    public ArrayList<Pair<Date, Integer>> getFinishnum() {
        return finishnum;
    }

    public Integer getTasknum() {
        return tasknum;
    }

    public ArrayList<Pair<Date, Integer>> getFailnum() {
        return failnum;
    }

    public void setFinishnum(ArrayList<Pair<Date, Integer>> finishnum) {
        this.finishnum = finishnum;
    }

    public void setTasknum(Integer tasknum) {
        this.tasknum = tasknum;
    }

    public void setFailnum(ArrayList<Pair<Date, Integer>> failnum) {
        this.failnum = failnum;
    }

    public void FinishInc(){
        Integer tmpCount =this.finishnum.get(this.finishnum.size()-1).getValue()+1;
        Date tmpDate = this.finishnum.get(this.finishnum.size()-1).getKey();
        Pair<Date,Integer> tmpTuple = new Pair<>(tmpDate,tmpCount);
        this.finishnum.set(this.finishnum.size()-1,tmpTuple);
    }

    public void FailInc(){
        Integer tmpCount =this.failnum.get(this.failnum.size()-1).getValue()+1;
        Date tmpDate = this.failnum.get(this.failnum.size()-1).getKey();
        Pair<Date,Integer> tmpTuple = new Pair<>(tmpDate,tmpCount);
        this.failnum.set(this.failnum.size()-1,tmpTuple);
    }
}
