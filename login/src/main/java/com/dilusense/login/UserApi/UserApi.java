package com.dilusense.login.UserApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dilusense.login.model.ModelInfo;
import com.dilusense.login.model.User;
import com.dilusense.login.model.VideoInfo;
import com.dilusense.login.service.ModelService;
import com.dilusense.login.service.UserService;
import com.dilusense.login.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserApi {

    @Autowired
    private ModelService modelService;

    @Autowired
    private VideoService videoService;

    @Resource
    private UserService userService;

    @Autowired
    public UserApi(UserService userService) {this.userService=userService;}

    @PostMapping("/getregist")
    public Object add(@RequestParam(required = true, value = "name") String name, @RequestParam(required = true, value = "password") String password, @RequestParam(required = false, value = "sex") String sex, @RequestParam(required = false, value = "email") String email, @RequestParam(required = true, value = "phone") String phone) {
        JSONObject jsonObject = new JSONObject();
        if (userService.findByName(name) != null) {
            jsonObject.put("message", "用户名被占用");
        } else {
            userService.add(name, password,sex,email,phone);
            jsonObject.put("message", "注册成功");
        }
        return jsonObject;
    }

    @GetMapping("/getlogin")
    public Object check(@RequestParam(required = true,value = "name") String name,@RequestParam(required = true,value="password") String password){
        Base64.Decoder decoder=Base64.getDecoder();
        JSONObject jsonObject = new JSONObject();
        User loginuser;
        if((loginuser=userService.check(name,new String(decoder.decode(password))))!=null) {
            jsonObject.put("message", "登陆成功");
            jsonObject.put("adminid",loginuser.getId().toString());
        } else{
            jsonObject.put("message","登陆失败");
        }
        return jsonObject;
    }

    @GetMapping("/getmodelinfo")
    public String getmodelinfo(@RequestParam(value = "user") String user)
    {
        JSONArray jsonArray = new JSONArray();
        Integer flag =0;
        List<ModelInfo> modelInfoList = modelService.getmodelinfo(user);
        for(ModelInfo element:
                modelInfoList)
        {
            JSONObject data = new JSONObject();
            data.put("name",element.getModelname());
            int tmp=element.getState();
            if(tmp==0)
            {
                data.put("state","Training model");
            }
            else if(tmp == 1)
            {
                data.put("state","Model generated");
            }
            else if(tmp == 2)
            {
                data.put("state","Model not generated");
            }
            if(flag ==0)
            {
                data.put("language","English");
            }
            else if(flag ==1)
            {
                data.put("language","Chinese");
            }
            else if(flag ==2)
            {
                data.put("language","Russian");
            }
            else
            {
                data.put("language","English");
            }
            flag++;
            jsonArray.add(data);
        }
        return jsonArray.toString();
    }

    @PostMapping("/videoupdate")
    public Object videoupdate(@RequestParam(value="file",required = false) MultipartFile file, String name, String groupname,String admin, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, InterruptedException {
        System.out.println("commit a video");
        String path = "/home/lc/resources/static/"+admin+"/videos/"+name;
        JSONObject jsonObject = new JSONObject();
        if(modelService.getBusystate()!=0 || videoService.getBusystate()!=0)
        {
            jsonObject.put("message","busy");
            return jsonObject;
        }
        if(modelService.getmodelinfobyName(name.substring(0,name.lastIndexOf(".")),admin).size()!=0)
        {
            jsonObject.put("message","Model exist");
            return jsonObject;
        }
        if(modelService.pathStore(path,file))
        {
            modelService.trainmodel(admin,name,path);
            if(modelService.insertModel(name,admin,groupname,path,0,"",name.substring(0,name.lastIndexOf(".")))!=0)    //第一个0，代表了当前都是0群组，第二个0是当前状态为正在生成模型
            {
                jsonObject.put("message","success");
            }
            else
            {
                jsonObject.put("message","mysql error");
            }
        }
        else
        {
            jsonObject.put("message","store error");
        }

        return jsonObject;
    }

    @PostMapping("/audioupdate")
    public Object audioupdate(@RequestParam(value="file",required = false) MultipartFile file, String name, String groupname,String admin,String model ,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, InterruptedException {
        System.out.println("commit a audio");
        JSONObject jsonObject = new JSONObject();
        String path = "/home/lc/resources/static/"+admin+"/audios/"+name;
        String videoName= model+"_"+name.substring(0,name.lastIndexOf("."))+".mp4";
        if(modelService.getBusystate()!=0 || videoService.getBusystate()!=0)
        {
            jsonObject.put("message","busy");
            return jsonObject;
        }
        if(videoService.getaudioinfoByname(name,admin).size()!=0)    //已生成视频
        {
            if(modelService.getmodelinfobyName(model,admin).size()!=0)
            {
                jsonObject.put("message","alerdy generated");
                return jsonObject;
            }
        }
        String videosrcpath = modelService.getmodelinfobyName(model,admin).get(0).getVideopath();
        if(videoService.pathStore(path,file))
        {
            videoService.generatevideo(admin,videoName,videosrcpath,path);
            String tmp=new String();
//            for(ModelInfo element:modelService.getmodelinfobyName(model,admin))  //理论上只有一个
//            {
//                System.out.println(element.getId());
//                tmp = element.getId()+"";
//            }
            tmp = modelService.getmodelinfobyName(model,admin).get(0).getId()+"";
            if(videoService.insertVideo(videoName,admin,groupname,tmp,"",0,"")!=0)
            {
                jsonObject.put("message","success");
            }
            else
            {
                jsonObject.put("message","mysql insert error");
            }
        }
        return jsonObject;
    }

    @GetMapping("/getvideolist")
    public String getvideolist(@RequestParam(value = "user",required = true) String user)
    {

        JSONArray jsonArray = new JSONArray();
        List<VideoInfo> videoInfoList = videoService.getvideoinfo(user);
        System.out.println(user);
        for(VideoInfo element:
                videoInfoList)
        {
            JSONObject data = new JSONObject();
            data.put("name",element.getName());
            data.put("path",element.getVideopath());
            int tmp = element.getState();
            if(tmp==0)
            {
                data.put("state","Generating video");
            }
            else if(tmp ==1)
            {
                data.put("state","Video generated");
            }
            else if(tmp ==2)
            {
                data.put("state","Fail");
            }
            else
            {
                data.put("state","Error");
            }
            jsonArray.add(data);
        }
        return jsonArray.toString();
    }

    @GetMapping("/delvideo")
    public Object deletevideo(@RequestParam(required = true,value="user")String user,@RequestParam(required = true,value="name")String name)
    {
        JSONObject jsonObject=new JSONObject();
        if(videoService.deleteVideo(user,name)!=0)
        {
            jsonObject.put("message","视频删除成功");
        }
        else
        {
            jsonObject.put("message","视频删除失败");
        }
        return jsonObject;
    }


    @GetMapping("/getvideofile")
    public void getvideofile(@RequestParam(value = "user") String user,@RequestParam(value = "name") String name,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String path = videoService.getvideopath(user, name);
        System.out.println(path);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fin = null;
                BufferedInputStream bin = null;
                try {
                    fin = new FileInputStream(file);
                    bin = new BufferedInputStream(fin);
                    byte[] buffer = new byte[bin.available()];
                    OutputStream os = response.getOutputStream();
                    int i = bin.read(buffer);
                    response.reset();
                    response.addHeader("Content-Disposition", "attachment;filename=" + name);
                    response.addHeader("Content-Length",""+file.length());
                    response.setContentType("application/octet-stream");
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bin.read(buffer);
                    }
                    System.out.println("downloaded");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bin != null) {
                        try {
                            bin.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fin != null) {
                        try {
                            fin.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
