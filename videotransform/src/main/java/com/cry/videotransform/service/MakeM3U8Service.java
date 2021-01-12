package com.cry.videotransform.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MakeM3U8Service {
    public boolean makem3u8(String path,String name,String serverurl) throws IOException {
        String filepath=path+name+".m3u8";
        File targetFile = new File(filepath);
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
        String content="#EXTM3U\n#EXT-X-STREAM-INF:BANDWIDTH=150000,RESOLUTION=416x234,CODECS=\"avc1.42e00a,mp4a.40.2\"\n"+serverurl+name+"_low.m3u8\n#EXTM3U\n#EXT-X-STREAM-INF:BANDWIDTH=240000,RESOLUTION=416x234,CODECS=\"avc1.42e00a,mp4a.40.2\"\n"+serverurl+name+"_mid.m3u8\n#EXTM3U\n#EXT-X-STREAM-INF:BANDWIDTH=440000,RESOLUTION=416x234,CODECS=\"avc1.42e00a,mp4a.40.2\"\n"+serverurl+name+"_high.m3u8";
        String m3u8data_low="#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:5\n#EXT-X-MEDIA-SEQUENCE:0";
        String m3u8data_mid="#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:5\n#EXT-X-MEDIA-SEQUENCE:0";
        String m3u8data_high="#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:5\n#EXT-X-MEDIA-SEQUENCE:0";
        File m3u8file_low = new File("/tmp/hls/low_"+name+".m3u8");                                      //这一步需要根据不同的nginx hls path来配置m3u8文件位置由于在本机开发，所以直接操作本机。
        File m3u8file_mid = new File("/tmp/hls/mid_"+name+".m3u8");
        File m3u8file_high = new File("/tmp/hls/high_"+name+".m3u8");
        try {
           FileOutputStream target = new FileOutputStream(targetFile);
           FileOutputStream target_low = new FileOutputStream(m3u8file_low);
           FileOutputStream target_mid = new FileOutputStream(m3u8file_mid);
           FileOutputStream target_high = new FileOutputStream(m3u8file_high);
           byte [] contentBytes = content.getBytes();
           byte [] m3u8lowBytes = m3u8data_low.getBytes();
           byte [] m3u8midBytes = m3u8data_mid.getBytes();
           byte [] m3u8highBytes = m3u8data_high.getBytes();
           target.write(contentBytes);
           target_low.write(m3u8lowBytes);
           target_mid.write(m3u8midBytes);
           target_high.write(m3u8highBytes);
           target.flush();
           target_low.flush();
           target_mid.flush();
           target_high.flush();
           target.close();
           target_low.close();
           target_mid.close();
           target_high.close();
       } catch (IOException e){
           e.printStackTrace();
       }
        return true;
    }
}
