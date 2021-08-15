package com.cry.admin_reg.UserApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cry.admin_reg.model.Group;
import com.cry.admin_reg.model.User;
import com.cry.admin_reg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserApi {
    @Resource
    private UserService userService;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registserver")
    public Object add(@RequestBody Map<String,String> data) {

        String name = data.get("name");
        String password = data.get("password");
        String sex = data.get("sex");
        String email =data.get("email");
        String phone = data.get("phone");
        JSONObject jsonObject = new JSONObject();
        if (userService.findByName(name) != null) {
            jsonObject.put("message", "用户名被占用");
        } else {
            userService.add(name, password,sex,email,phone);
            jsonObject.put("message", "注册成功");
        }
        return jsonObject;
    }

    @GetMapping("/loginserver")
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

    @PostMapping("/changeserver")
    public Object change(@RequestBody Map<String,String> data) {

        String name = data.get("name");
        String oldpassword = data.get("oldPassword");
        String newpassword = data.get("newPassword");
        String email =data.get("email");
        String phone = data.get("phone");
        JSONObject jsonObject = new JSONObject();
        if (userService.check(name,oldpassword) == null) {
            jsonObject.put("message", "账号信息修改失败");
        } else {
            userService.change(name,newpassword,email,phone);
            jsonObject.put("message", "账号信息修改成功");
        }
        return jsonObject;
    }

    @GetMapping("/getgroupInfo")
    public Object getgroupInfo(@RequestParam(required = true,value = "user") String user){
        List<String> userInfo=userService.selectGroup(user);
        JSONObject jsonObject = new JSONObject();
        if(userInfo ==null||userInfo.isEmpty())
        {
            jsonObject.put("message","当前未创建群组");
        }
        else
        {
            jsonObject.put("groupdata",userInfo);
            jsonObject.put("message","群组数据传递成功");
        }
        return jsonObject;
    }

    @GetMapping("/getgroupList")
    public String getgroupList(@RequestParam(required = true,value = "user") String user){
        List<Group> userlist=userService.selectGroupList(user);
        JSONArray jsonArray = new JSONArray();

        for (Group element:
             userlist) {
            JSONObject data= new JSONObject();
            data.put("groupname",element.getName());
            if(element.getDescribe()=="")
            {
                data.put("groupdescribe","NULL");
            }
            else {
                data.put("groupdescribe", element.getDescribe());
            }
            System.out.println(data);
            jsonArray.add(data);
        }
        System.out.println(jsonArray.toString());
        return jsonArray.toString();
    }

    @GetMapping("/addgroup")
    public Object addgroup(@RequestParam(required = true,value="name") String name,@RequestParam(required = true,value="user") String user,@RequestParam(required = false,value="describe") String describe)
    {
        JSONObject jsonObject=new JSONObject();
        if(userService.findGroupByname(name,user)!=null)
        {
            jsonObject.put("message","群组名有冲突");
        }
        else
        {
            if(userService.insertGroup(name,user,describe)!=0)
            {
                jsonObject.put("message","群组创建成功");
            }
            else
            {
                jsonObject.put("message","插入失败");
            }
        }
        return jsonObject;
    }


    @GetMapping("/deletegroup")
    public Object deletegroup(@RequestParam(required = true,value = "name")String name,@RequestParam(required = true,value = "user") String user){
        JSONObject jsonObject=new JSONObject();
        if(userService.deleteGroup(name,user) ==1)
        {
            jsonObject.put("message","数组删除成功");
        }
        else
        {
            jsonObject.put("message","数组删除失败");
        }
        return jsonObject;
    }
}
