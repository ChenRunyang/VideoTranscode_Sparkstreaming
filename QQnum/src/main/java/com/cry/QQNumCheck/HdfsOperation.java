package com.cry.QQNumCheck;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.web.resources.ExceptionHandler;

import java.io.*;
import java.net.URI;
import java.io.Serializable;

public class HdfsOperation implements Serializable {
    private static Configuration conf = new Configuration();
    private static BufferedWriter writer = null;

    //在hdfs的目标位置新建一个文件，得到一个输出流
    public static void openHdfsFile(String path) throws Exception {
        FileSystem fs = FileSystem.get(URI.create(path),conf);
        writer = new BufferedWriter(new OutputStreamWriter(fs.create(new Path(path))));
        if(writer==null){
            System.out.println("system init fail");
        }
    }

    //往hdfs文件中写入数据
    public static void writeString(String line) {
        try {
            writer.write(line + "\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //关闭hdfs输出流
    public static void closeHdfsFile() {
        try {
            if (writer!=null) {
                writer.close();
            }
            else{
                System.out.println("Close Hdfs error");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
