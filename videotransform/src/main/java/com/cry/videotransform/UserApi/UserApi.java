package com.cry.videotransform.UserApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cry.videotransform.model.VideoInfo;
import com.cry.videotransform.service.MakeM3U8Service;
import com.cry.videotransform.service.VideoService;
import com.cry.videotransform.service.fileStore;
import com.cry.videotransform.service.fileTrans;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import static com.cry.videotransform.service.ProducerService.getProducerService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserApi {

    @Autowired
    fileStore filestore;

    @Autowired
    VideoService videoService;

    @Autowired
    fileTrans filetrans;

    @Autowired
    MakeM3U8Service makeM3U8Service;


    @PostMapping("/videoupdate")
    public Object videoupdate(@RequestParam(value="file",required = false) MultipartFile file, String name,String groupname, String admin, HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        System.out.println("connect a video");
        String path = "src/main/resources/static/" + admin + "/videos/" +groupname;
        String videopath=path +"/" +name;
        System.out.println(admin + "and" + groupname+"and"+name);
        JSONObject jsonObject = new JSONObject();
        if(filestore.storeropath(videopath, file))
        {
            FFmpegFrameGrabber grabber=FFmpegFrameGrabber.createDefault(videopath);
            grabber.start();
            Frame frame=grabber.grabImage();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage img = converter.getBufferedImage(frame);
            int videotime=(int)grabber.getLengthInTime();
            String sendtime = new java.sql.Date(new java.util.Date().getTime()).toString();
            videoService.insertVideo(name,videopath,videotime,groupname,sendtime,admin,img.getWidth(),img.getHeight());
            jsonObject.put("message", "视频上传成功");
        }
        return jsonObject;
    }

    @PostMapping("/pictureupdate")
    public  Object pictureupdate(@RequestParam(value="file",required = false) MultipartFile file,String videoname,String seq,String admin,HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        System.out.println("connect a picture");
        String path = "src/main/resources/static/" + admin + "/pictures/" + videoname+seq+".jpg";
        JSONObject jsonObject = new JSONObject();
        if (filestore.storeropath(path, file)) {
            jsonObject.put("message", "截图成功");
        }
        return jsonObject;
    }

    @GetMapping("/getvideoinfo")
    public String getvideoinfo(@RequestParam(value = "user") String user)
    {
        JSONArray jsonArray = new JSONArray();
        List<VideoInfo>  videoinfo=videoService.getVideoInfo(user);
        for (VideoInfo element:
             videoinfo) {
                JSONObject data = new JSONObject();
                data.put("name",element.getName());
                data.put("time",(element.getVideotime()+""));
                data.put("groupName",videoService.getgroupname(element.getGroupid()));
                data.put("updateTime",element.getSendtime());
                data.put("size",(element.getWidth()+"*"+element.getHeight()));
                jsonArray.add(data);
        }
        return jsonArray.toString();
    }

    @GetMapping("/delvideo")
    public Object delvideo(@RequestParam(value = "user") String user,@RequestParam(value = "name") String name)
    {
        JSONObject jsonObject=new JSONObject();
        String videopath;
        if(!(videopath=videoService.getvideopath(user,name)).isEmpty())
        {
            if(filestore.deletepath(videopath))
            {
                if(videoService.delvideo(user,name)==1)
                {
                    jsonObject.put("message","数据删除成功");
                }
                else
                {
                    jsonObject.put("message","数据删除失败");
                }
            }
            else
            {
                jsonObject.put("message","数据删除失败");
            }
        }
        else
        {
            jsonObject.put("message","数据删除失败");
        }
        return jsonObject;
    }

    @GetMapping("/startlive")
    public Object startlive(@RequestParam(value="user") String user,@RequestParam(value = "name") String name,@RequestParam(value = "password") String password,@RequestParam(value = "serverurl") String serverurl) throws IOException {
        JSONObject jsonObject=new JSONObject();
        String path="src/main/resources/static/"+user+"/live/M3U8List/";
        if(makeM3U8Service.makem3u8(path,name,serverurl))
        {
            if(videoService.checkstate(user)==null)
            {
                if(videoService.insertroom(user,name,password)!=0)
                {
                    jsonObject.put("message","成功");
                }
                else
                {
                    jsonObject.put("message","信息插入失败");
                }
            }
            else
            {
                jsonObject.put("message","查询状态失败");
            }
        }
        else
        {
            jsonObject.put("message","建立m3u8文件失败");
        }
        return jsonObject;
    }

    @PostMapping("/liveupdate")
    public Object liveupdate(@RequestParam(value ="file",required = false) MultipartFile file,String name,String seq,String admin, HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String filename=admin+"_"+name+seq;
        byte[] videobytes=file.getBytes();
        getProducerService().sendData(filename,videobytes);    //送进消息队列中，进行转码
        String path= "src/main/resources/static/" + admin + "/live/" ;
        String livepath=path +"/" +name+seq;
        if(filestore.storeropath(livepath,file))
        {
            jsonObject.put("message","上传成功");
        }
        else
        {
            jsonObject.put("message","上传失败");
        }
        return jsonObject;
    }

    @GetMapping("/watchlive")
    public Object watchlive(@RequestParam(value = "user",required = true) String user,@RequestParam(value = "password",required = false) String password)
    {
        JSONObject jsonObject = new JSONObject();
        String m3u8_filepath = videoService.getliveroom(user,password);
        if(m3u8_filepath!=null)
        {
            jsonObject.put("message",m3u8_filepath);
        }
        else
        {
            jsonObject.put("message","未找到");
        }
        return jsonObject;
    }

    @GetMapping("/stoplive")
    public Object stoplive(@RequestParam(value = "room") String room,@RequestParam(value = "password") String password) {
        JSONObject jsonObject = new JSONObject();
        String m3u8path = videoService.getliveroom(room, password);
        if (videoService.delroom(room) != 0) {
            if (filestore.deletepath(m3u8path)) {
                jsonObject.put("message", "成功");
            } else {
                jsonObject.put("message", "m3u8删除失败");
            }
        }
        else
        {
            jsonObject.put("message","数据库删除失败");
        }
        return jsonObject;
    }

    @GetMapping("/getvideofile")
    public Object getvideofile(@RequestParam(value = "user") String user, @RequestParam(value = "name") String name,HttpServletRequest request, HttpServletResponse response, HttpSession session){
        JSONObject jsonObject = new JSONObject();
        String filepath = videoService.getvideopath(user,name);
        if(filepath != null){
            File file = new File(filepath);
            if(file.exists()){
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName="+name);
                byte[] buffer = new byte[1024];
                FileInputStream fin =null;
                BufferedInputStream bin=null;
                try{
                    fin = new FileInputStream(file);
                    bin = new BufferedInputStream(fin);
                    OutputStream os = response.getOutputStream();
                    int i =bin.read(buffer);
                    while(i != -1){
                        os.write(buffer,0,i);
                        i=bin.read(buffer);
                    }
                    jsonObject.put("message","下载成功");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(bin !=null){
                        try {
                            bin.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fin != null){
                        try {
                            fin.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return jsonObject;
    }

    @GetMapping("/getvideopath")
    public Object getvideopath(@RequestParam(value = "user") String user, @RequestParam(value = "name") String name){
        JSONObject jsonObject= new JSONObject();
        String tmp=videoService.getvideopath(user,name);
        jsonObject.put("message","success");
        jsonObject.put("path","tmp");
        return jsonObject;
    }

    @GetMapping("/transformvideo")
    public Object transformvideo(@RequestParam(value = "user") String user, @RequestParam(value = "name") String name, @RequestParam(value = "codec") String codec,@RequestParam(value = "format") String format) throws FrameGrabber.Exception, FileNotFoundException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        String path = videoService.getvideopath(user,name);
        ArrayList<String> dividefiles=fileTrans.divideFile(path,format,300);  //视频分割(每五分钟切割一次)
        for(String dividedfile:dividefiles)
        {
            File file=new File(dividedfile);
            byte[] videobytes=dividedfile.getBytes();
            if(codec.equals("H264"))
            {
                getProducerService().sendH264Data(dividedfile,videobytes);
            }
            else if(codec.equals("AVS"))
            {
                getProducerService().sendAVSData(dividedfile,videobytes);
            }
            else if(codec.equals("VP8"))
            {
                getProducerService().sendVP8Data(dividedfile,videobytes);
            }
            else
            {
                jsonObject.put("message","格式不支持");
                break;
            }
            filestore.addhash(path,dividedfile);
            filestore.mergevideo(path,codec);
        }
        return jsonObject;
    }
}
