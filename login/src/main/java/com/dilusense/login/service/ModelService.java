package com.dilusense.login.service;

import com.dilusense.login.Dao.UserDao;
import com.dilusense.login.model.ModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

@Service
public class ModelService {

    @Resource
    private UserDao userDao;

    @Autowired
    public ModelService(UserDao userDao){
        this.userDao=userDao;
    }

    public int insertModel(String name,String admin,String group,String videopath,int state,String modelpath,String modelname)
    {
        int userid = Integer.parseInt(admin);
        int groupid = Integer.parseInt(group);
        return userDao.insertmodel(name,userid,groupid,videopath,state,modelpath,modelname);
    }
    public List<ModelInfo> getmodelinfo(String user)
    {
        int userid = Integer.parseInt(user);
        return userDao.getmodelinfo(userid);
    }

    public List<ModelInfo> getmodelinfobyName(String modelname,String user)
    {
        int userid = Integer.parseInt(user);
        return userDao.getmodelinfobyName(modelname,userid);
    }

    public boolean pathStore(String path, MultipartFile file) throws IOException{
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

    public int getBusystate()
    {
        return userDao.getBusymodel();
    }

    @Async("modelTrain")
    public void trainmodel(String admin,String name,String path) throws InterruptedException {
        int userid = Integer.parseInt(admin);
        BufferedReader br = null;
        Integer state =new Integer(0);
        try {
            Process p = Runtime.getRuntime().exec("bash /home/lc/scripts/train.sh "+path);    //脚本命令
            //Process p = Runtime.getRuntime().exec("bash /home/lc/scripts/hello.sh");    //脚本命令
            state = p.waitFor();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
            p.destroy();
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
        if(state.equals(0))
        {
            if(userDao.updateModelpath("placeholder",userid,name)!=0)
            {
                if(userDao.updateModelstate(userid,name,1)!=0)
                {
                    System.out.println("Model generated");
                }
                else
                {
                    System.out.println("update success model state error");
                }
            }
            else
            {
                System.out.println("update model path error");
            }
        }
        else
        {
            if(userDao.updateModelstate(userid,name,2)!=0)
            {
                System.out.println("update fail model state error :"+state);
            }
            else
            {
                System.out.println("generate model fail :" +state);
            }
        }

    }
}
