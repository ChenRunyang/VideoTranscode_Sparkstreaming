package com.cry.videotransform.service;

import javafx.util.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.System.exit;

@Service
public class fileStore {

    private ArrayList<Pair<String,String>> tmpTime;
    private HashMap<String,Date> startTime;

    @Autowired
    private static RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    public fileStore(RedisTemplate<String,Object> redisTemplate)
    {
        this.redisTemplate=redisTemplate;
    }

    public boolean storeropath(String path, MultipartFile file) throws IOException {
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

    public boolean deletepath(String path)
    {
        File targetFile = new File(path);
        try{
            if(targetFile.isFile())
            {
                if(targetFile.delete())
                {
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addhash(String KeyName,String NodeName) throws IOException {
        FileWriter fw =null;
        redisTemplate.opsForSet().add(KeyName,NodeName);
        File f=new File(KeyName.split(".")[0]+".txt");
        fw =new FileWriter(f,true);
        PrintWriter pw =new PrintWriter(fw);
        pw.println("file \'"+NodeName+"\'");    //为了以后方便合并分割的视频
        pw.flush();
        try{
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            p.waitFor();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deletetmp(String det)
    {
        try(FileReader reader = new FileReader(det);
        BufferedReader br = new BufferedReader(reader))
        {
            String tmp;
            while((tmp = br.readLine())!=null)
            {
                deletepath(tmp);    //删除切片的视频片段
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getcurrect()
    {
        return redisTemplate.keys("*").size();                 //全部进行查找
    }

    public HashMap<String, Date> getStartTime() {
        return startTime;
    }

    public void setStartTime(HashMap<String, Date> startTime) {
        this.startTime = startTime;
    }

    public ArrayList<Pair<String, String>> getTmpTime() {
        return tmpTime;
    }

    public void setTmpTime(ArrayList<Pair<String, String>> tmpTime) {
        this.tmpTime = tmpTime;
    }

    @KafkaListener(id="H264",topics="H264Que")
    public void processH264(ConsumerRecord<String, byte[]> transcodedfile) throws IOException {
        File targetFile=new File(transcodedfile.key());
        try {
            if(!targetFile.getParentFile().exists()) {
                if(!targetFile.getParentFile().mkdirs())
                {
                    System.out.println("mkdir error");
                    exit(1);
                }
            }
            if(targetFile.exists())
            {
                targetFile.delete();
            }
            if(!targetFile.createNewFile())
            {
                System.out.println("create file error");
                exit(2);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        OutputStream out = null;
        int length;
        try {
            out = new BufferedOutputStream(new FileOutputStream((targetFile)));
            out.write(transcodedfile.value(),0,transcodedfile.value().length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            redisTemplate.opsForSet().remove(transcodedfile.key().split(".")[0],transcodedfile.key());
            this.notify();
        }
    }

    @KafkaListener(id="AVS",topics="AVSQue")
    public void processAVS(ConsumerRecord<String, byte[]> transcodedfile) throws IOException {
        File targetFile=new File(transcodedfile.key());
        try {
            if(!targetFile.getParentFile().exists()) {
                if(!targetFile.getParentFile().mkdirs())
                {
                    System.out.println("mkdir error");
                    exit(1);
                }
            }
            if(targetFile.exists())
            {
                targetFile.delete();
            }
            if(!targetFile.createNewFile())
            {
                System.out.println("create file error");
                exit(2);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        OutputStream out = null;
        int length;
        try {
            out = new BufferedOutputStream(new FileOutputStream((targetFile)));
            out.write(transcodedfile.value(),0,transcodedfile.value().length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            redisTemplate.opsForSet().remove(transcodedfile.key().split(".")[0],transcodedfile.key());
            this.notify();
        }
    }

    @KafkaListener(id="VP8",topics="VP8Que")
    public void processVP8(ConsumerRecord<String, byte[]> transcodedfile) throws IOException {
        File targetFile=new File(transcodedfile.key());
        try {
            if(!targetFile.getParentFile().exists()) {
                if(!targetFile.getParentFile().mkdirs())
                {
                    System.out.println("mkdir error");
                    exit(1);
                }
            }
            if(targetFile.exists())
            {
                targetFile.delete();
            }
            if(!targetFile.createNewFile())
            {
                System.out.println("create file error");
                exit(2);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        OutputStream out = null;
        int length;
        try {
            out = new BufferedOutputStream(new FileOutputStream((targetFile)));
            out.write(transcodedfile.value(),0,transcodedfile.value().length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            redisTemplate.opsForSet().remove(transcodedfile.key().split(".")[0],transcodedfile.key());
            this.notify();
        }
    }

    @Async("mergeThreadPoll")
    public void mergevideo(String path,String codec) throws InterruptedException {     //此处可以使用简单工厂的设计模式
        System.out.println("merging video");
        MessageListenerContainer merger=registry.getListenerContainer(codec);
        synchronized (merger){
            if(!merger.isRunning())
            {
                System.out.println("Receiving data");
                merger.start();
            }
        }
        while(redisTemplate.opsForSet().size(path)!=0)    //等转码后的数据
        {
            merger.wait(3000);        //超时或收到已转码信息
        }
        exeCmd("ffmpeg -f concat -i "+path.split(".")[0]+".txt -c copy "+path);    //合并转码文件
        deletetmp(path.split(".")[0]+".txt");
        deletepath(path.split(".")[0]+".txt");
    }
}
