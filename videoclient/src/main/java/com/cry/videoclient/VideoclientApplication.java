package com.cry.videoclient;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import scala.Tuple2;


import org.apache.spark.SparkConf;

import org.apache.spark.streaming.Durations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


public class VideoclientApplication {
    public static void main(String[] args) throws InterruptedException {

        //JavaStre]=-[[[=-p0o9i8u7y6t5r4ew3q21  `-0p-[[po0[/.,./.,p]-098763524  SDk;][-0amingContext对象，它是流功能的主要入口点。使用两个执行线程创建一个本地StreamingContext，批处理间隔为1秒。
        //SparkStreaming 中local后必须为大于等于2的数字【即至少2条线程】。因为receiver 占了一个不断循环接收数据
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(1));

        // 创建一个DStream来表示来自TCP源的流数据，指定为主机名（例如localhost）和端口（例如9999）
        String brokers="10.1.2.50:9092";                 //注意，此处不能填127.0.0.1

        Map<String,Object> kafkaParams=new HashMap<>();
        kafkaParams.put("metadata.broker.list",brokers);
        kafkaParams.put("bootstrap.servers",brokers);
        kafkaParams.put("group.id","transCli");
        kafkaParams.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer","org.apache.kafka.common.serialization.ByteArrayDeserializer");
        kafkaParams.put("auto.offset.reset","latest");

        Collection<String> topics = Arrays.asList("transQue");
        JavaInputDStream<ConsumerRecord<String, byte[]>> data= KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(topics,kafkaParams));
        data.foreachRDD(new VoidFunction<JavaRDD<ConsumerRecord<String, byte[]>>>() {
            @Override
            public void call(JavaRDD<ConsumerRecord<String, byte[]>> consumerRecordJavaRDD) throws Exception {
                consumerRecordJavaRDD.foreach(new VoidFunction<ConsumerRecord<String, byte[]>>() {
                    @Override
                    public void call(ConsumerRecord<String, byte[]> consumerRecord) throws Exception {
                        System.out.println(consumerRecord.key()+"and"+consumerRecord.value());
                        InputStream inputStream = new ByteArrayInputStream(consumerRecord.value());
                        FrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
                        grabber.start();
                        FFmpegFrameRecorder recorder_mid = new FFmpegFrameRecorder("/tmp/hls/mid_"+consumerRecord.key()+".ts",grabber.getImageWidth(),grabber.getImageHeight(),1);    //音频支持，filename为推送地址，根据不同环境进行修改
                        FFmpegFrameRecorder recorder_low = new FFmpegFrameRecorder("/tmp/hls/low_"+consumerRecord.key()+".ts",grabber.getImageWidth(),grabber.getImageHeight(),1);    //音频支持，filename为推送地址，根据不同环境进行修改
                        recorder_low.setInterleaved(true);  //增加并发率
                        recorder_mid.setInterleaved(true);
                        recorder_low.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                        recorder_mid.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                        recorder_low.setFormat("mpegts");
                        recorder_mid.setFormat("mpegts");
                        recorder_low.setFrameRate(25);
                        recorder_mid.setFrameRate(25);
                        recorder_low.setVideoQuality(0.5);
                        recorder_mid.setVideoQuality(0.8);
                        recorder_low.setPixelFormat(0);            //yuv420
                        recorder_mid.setPixelFormat(0);
                        recorder_low.start();
                        recorder_mid.start();
                        Frame captured_frame = null;
                        while(true){
                            try{
                                captured_frame = grabber.grabFrame();
                                if(captured_frame ==null)
                                {
                                    System.out.println("A video_segment transfored");
                                    break;
                                }
                                recorder_low.record(captured_frame);
                                recorder_mid.record(captured_frame);
                            }catch (FrameGrabber.Exception e){
                                e.printStackTrace();
                            }
                        }
                        recorder_low.close();
                        recorder_mid.close();
                        grabber.close();
                        System.out.println("Transformation done");
                        System.out.println(consumerRecord.value());
                    }
                });
            }
        });

//        JavaPairDStream<String,byte[]> transfordata= data.transform(new Function<ConsumerRecord<String,byte[]>, JavaPairRDD<String,byte[]>>(){
//            @Override
//            public JavaPairRDD<String, byte[]> call(ConsumerRecord<String,byte[]> consumerRecord) throws Exception{
//                Tuple2<String,byte[]> rdd=new Tuple2<String,byte[]>(consumerRecord.key(),consumerRecord.value());
//            }
//        });



        jsc.start();              // 开始
        jsc.awaitTermination();   // 等待计算终止
        jsc.close();
    }
}