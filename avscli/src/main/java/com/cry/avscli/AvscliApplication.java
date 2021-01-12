package com.cry.avscli;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AvscliApplication {

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("NetworkWordCount");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(1));

        // 创建一个DStream来表示来自Kafka源的流数据，指定为主机名（例如localhost）和端口（例如9999）
        String brokers="10.1.2.50:9092";                 //注意，此处不能填127.0.0.1,可根据具体情况进行修改
        Map<String,Object> kafkaParams=new HashMap<>();
        kafkaParams.put("metadata.broker.list",brokers);
        kafkaParams.put("bootstrap.servers",brokers);
        kafkaParams.put("group.id","AVSCli");
        kafkaParams.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer","org.apache.kafka.common.serialization.ByteArrayDeserializer");
        kafkaParams.put("auto.offset.reset","latest");

        Collection<String> topics = Arrays.asList("AVSQue");
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
                        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(consumerRecord.key()+".mp4",grabber.getImageWidth(),grabber.getImageHeight(),1);    //音频支持，filename为推送地址，根据不同环境进行修改
                        recorder.setInterleaved(true);
                        recorder.setVideoCodec(avcodec.AV_CODEC_ID_AVS);
                        recorder.setFormat("mp4");
                        if(consumerRecord.key().split(".")[0].indexOf(consumerRecord.key().split(".")[0].length()-1)=='t')    //fast
                        {
                            recorder.setFrameRate(20);
                            recorder.setVideoQuality(0.5);
                        } else if(consumerRecord.key().split(".")[0].indexOf(consumerRecord.key().split(".")[0].length()-1)=='l')    //normal
                        {
                            recorder.setFrameRate(25);
                            recorder.setVideoQuality(0.8);
                        }
                        else    //slow
                        {
                            recorder.setFrameRate(25);
                            recorder.setVideoQuality(1.0);
                        }
                        recorder.setPixelFormat(0);
                        recorder.start();
                        Frame captured_frame = null;
                        while(true){
                            try{
                                captured_frame = grabber.grabFrame();
                                if(captured_frame ==null)
                                {
                                    System.out.println("A video_segment transfored");
                                    break;
                                }
                                recorder.record(captured_frame);
                            }catch (FrameGrabber.Exception e){
                                e.printStackTrace();
                            }
                        }
                        recorder.close();
                        grabber.close();
                        System.out.println("Transformation done");
                        System.out.println(consumerRecord.value());
                    }
                });
            }
        });

        jsc.start();              // 开始
        jsc.awaitTermination();   // 等待计算终止
        jsc.close();
    }

}
