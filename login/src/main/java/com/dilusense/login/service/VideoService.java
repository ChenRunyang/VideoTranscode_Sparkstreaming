package com.dilusense.login.service;

import com.dilusense.login.Dao.UserDao;
import com.dilusense.login.model.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

@Service
public class VideoService {

    @Resource
    private UserDao userDao;

    @Autowired
    public VideoService(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public List<VideoInfo> getaudioinfoByname(String name,String user)
    {
        int userid = Integer.parseInt(user);
        return userDao.getaudioinfobyName(name,userid);
    }

    public int insertVideo(String name,String user,String group,String model,String videopath,int state,String serverpath)
    {
        int userid=Integer.parseInt(user);
        int groupid = Integer.parseInt(group);
        int modelid = Integer.parseInt(model);
        return userDao.insertvideo(name,userid,groupid,modelid,videopath,state,serverpath);
    }

    public boolean pathStore(String path, MultipartFile file) throws IOException {
        File targetFile = new File(path);
        try {
            if(!targetFile.getParentFile().exists()) {
                if(!targetFile.getParentFile().mkdirs())
                {
                    System.out.println("mkdir error");
                    return false;
                }
            }
            if(targetFile.exists())
            {
                targetFile.delete();
            }
            if(!targetFile.createNewFile())
            {
                System.out.println("create file error");
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        OutputStream out = null;
        InputStream in = null;
        int length;
        try {
            out = new BufferedOutputStream(new FileOutputStream((targetFile)));
            in = new BufferedInputStream(file.getInputStream());
            byte[] buf = file.getBytes();
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
        }
        return true;
    }

    public List<VideoInfo> getvideoinfo(String user)
    {
        int userid= Integer.parseInt(user);
        return userDao.getvideoinfobyUser(userid);
    }

    public String getvideopath(String user,String name)
    {
        int userid = Integer.parseInt(user);
        List<VideoInfo> tmp =userDao.getaudioinfobyName(name,userid);
        return tmp.get(0).getServerpath();
    }

    public int deleteVideo(String user,String name)
    {
        String path_Audio ="/home/lc/resources/static/"+user+"/generated_videos/"+name;
        String path_Noaudio="/home/lc/resources/static/"+user+"/generated_videos/"+name.substring(0,name.lastIndexOf("."))+"_no_audio.mp4";
        File targetFile = new File(path_Audio);
        File targetFile1 = new File(path_Noaudio);
        if(targetFile.exists())
        {
            try {
                if(targetFile.isFile())
                {
                    if(!targetFile.delete())
                    {
                        return 0;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(targetFile1.exists())
        {
            try {
                if(targetFile1.isFile())
                {
                    if(!targetFile1.delete())
                    {
                        return 0;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        int userid = Integer.parseInt(user);
        return userDao.deleteVideo(userid,name);
    }

    public int getBusystate()
    {
        return userDao.getBusyvideo();
    }

    @Async("modelTrain")
    public void generatevideo(String admin,String name,String videosrcpath,String audiopath) throws InterruptedException {
        int userid = Integer.parseInt(admin);
        System.out.println("generating video");
        BufferedReader br = null;
        Integer state = 0;
        try {
            Process p = Runtime.getRuntime().exec("bash /home/lc/scripts/infer.sh "+videosrcpath+" "+audiopath);    //脚本命令
            state = p.waitFor();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println(sb.toString());
            if(!state.equals(0))
            {
                br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                System.out.println(sb.toString());
                br.close();
            }
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(state.equals(0))
        {
            try {
                Process t = Runtime.getRuntime().exec("ffmpeg -i "+"/home/lc/resources/static/"+admin+"/generated_videos/"+name+" -strict -2 -f hls "+"/home/lc/videosrc/"+name.substring(0,name.lastIndexOf("."))+".m3u8");    //脚本命令
                state = t.waitFor();
                br = new BufferedReader(new InputStreamReader(t.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                System.out.println(sb.toString());
                if(!state.equals(0))
                {
                    br = new BufferedReader(new InputStreamReader(t.getErrorStream()));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    System.out.println(sb.toString());
                    br.close();
                }
                t.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally
            {
                if (br != null)
                {
                    try {
                        br.close();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(state.equals(0))
        {
            if(userDao.updateVideopath("http://10.1.2.172:8013/hls/"+name.substring(0,name.lastIndexOf("."))+".m3u8",userid,name)!=0)
            {
                if(userDao.updateServerpath("/home/lc/resources/static/"+admin+"/generated_videos/"+name,userid,name)!=0)
                {
                    if(userDao.updateVideostate(userid,name,1)!=0)
                    {
                        System.out.println("Video generated");
                    }
                    else
                    {
                        System.out.println("update success state error");
                    }
                }
                else
                {
                    System.out.println("update server error");
                }
            }
            else
            {
                System.out.println("update video path error");
            }
        }
        else
        {
            if(userDao.updateVideostate(userid,name,2)!=0)
            {
                System.out.println("video generate error "+state);
            }
            else
            {
                System.out.println("update fail video state error "+state);
            }
        }
    }
}
