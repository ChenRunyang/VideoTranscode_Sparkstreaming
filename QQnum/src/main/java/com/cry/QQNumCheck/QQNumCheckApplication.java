package com.cry.QQNumCheck;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import org.apache.spark.SparkConf;

import org.apache.spark.streaming.Durations;

import java.text.SimpleDateFormat;
import java.util.*;

//用户输入目录:tmp,实际检测目录Data
public class QQNumCheckApplication {
    //使用单例模式，防止持续新建累加器
    private static volatile myCollectionAccumulator.CollectionAccumulator<Date> collectionAccumulator =null;

    public static myCollectionAccumulator.CollectionAccumulator<Date> getInstance(JavaSparkContext jc)
    {
        if(collectionAccumulator ==null)
        {
            synchronized (QQNumCheckApplication.class)
            {
                if(collectionAccumulator ==null)
                {
                    collectionAccumulator =new myCollectionAccumulator.CollectionAccumulator<>();
                    jc.sc().register(collectionAccumulator,"NewestTime");
                }
            }
        }
        return collectionAccumulator;
    }

    public static void main(String[] args) throws Exception
    {

        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("QQNumCheck");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(10));    //时间需要小于log文件的更新时间，防止丢失数据
        JavaDStream<String> logs =jsc.textFileStream("/Users/chenrunyang/Data");
        jsc.checkpoint("./cache");  //状态缓存文件夹

        //使用MapToPair算子将源文件分成QQ号和登陆时间的键值对
        JavaPairDStream<Integer,Date> pairlogs=logs.mapToPair(new PairFunction<String, Integer, Date>()
        {
            @Override
            public Tuple2<Integer, Date> call(String s) throws Exception
            {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date time = new Date();
                Integer QQnum = Integer.valueOf(s.toString().split(" ")[1]);
                String time_string = s.toString().split(" ")[0].split("T")[0]+" "+s.toString().split(" ")[0].split("T")[1];
                try
                {
                    time=ft.parse(time_string);
                    //collectionAccumulator.add(time);
                }catch (Exception e)
                {
                    System.out.println("Parse date error");
                }
                return new Tuple2<Integer,Date>(QQnum,time);
            }
        });

        //使用自定义累加器得到当前数据最新时间
        pairlogs.foreachRDD((rdd,time)->{
            myCollectionAccumulator.CollectionAccumulator<Date> collectionAccumulator=getInstance(new JavaSparkContext((rdd.context())));
            rdd.foreach((rdddata)->{
                if(rdddata._2.after(collectionAccumulator.value()))
                {
                    collectionAccumulator.add(rdddata._2);        //得到最近的时间，方便后续过滤已过期的QQ号码
                }
            });
        });

        //过滤掉二十分钟前的数据
        JavaPairDStream<Integer,Date> filted_pairlogs=pairlogs.filter(new Function<Tuple2<Integer, Date>, Boolean>() {
            @Override
            public Boolean call(Tuple2<Integer, Date> integerDateTuple2) throws Exception {
                return ((collectionAccumulator.value().getTime()-integerDateTuple2._2.getTime())<=20*60*1000);         //20分钟前的数据进行清除
            }
        });

        //检测时间间隔小于五分钟的QQ号码
        JavaPairDStream<Integer,Tuple2<Date, Boolean>> results = filted_pairlogs.updateStateByKey(new Function2<List<Date>, Optional<Tuple2<Date, Boolean>>, Optional<Tuple2<Date, Boolean>>>() {
            @Override
            public Optional<Tuple2<Date, Boolean>> call(List<Date> dates, Optional<Tuple2<Date, Boolean>> state) throws Exception {
                Tuple2<Date,Boolean> newresult = null;
                PriorityQueue<Long> timegap = new PriorityQueue<>();    //使用升序优先队列，只要第一个元素大于五分钟即通过
                Date tmp_predate=new Date(1);    //初始化一个小值，保证date[0]和tmp相减必大于五分钟
                Date tmp_latest=new Date(1);
                if(state.isPresent())
                {
                    tmp_latest = state.get()._1;
                    tmp_predate = state.get()._1;
                }
                if(dates.size()>0)      //这一批次出现此值
                {
                    for(Date date:dates)
                    {
                        timegap.add(date.getTime()-tmp_predate.getTime());
                        tmp_predate=date;
                        if(date.after(tmp_latest))
                        {
                            tmp_latest=date;
                        }
                    }
                }
                else    //这批次没有此值
                {
                    timegap.add((long) (6*60*1000));    //添加一个大于5分钟的值即可
                }

                if(!timegap.isEmpty())
                {
                    if(timegap.poll()<5*60*1000)
                    {
                        newresult=new Tuple2<Date,Boolean>(tmp_latest,true);
                    }
                    else
                    {
                        newresult = new Tuple2<Date,Boolean>(tmp_latest,false);
                    }
                }
                else
                {
                    System.out.println("Result update error");
                }

                return Optional.of(newresult);
            }
        });

        //得到这批掉线QQ号的号码
        JavaDStream<Integer> output = results.transform(new Function<JavaPairRDD<Integer, Tuple2<Date, Boolean>>, JavaRDD<Integer>>() {
            @Override
            public JavaRDD<Integer> call(JavaPairRDD<Integer, Tuple2<Date, Boolean>> integerTuple2JavaPairRDD) throws Exception {
                JavaPairRDD<Integer,Tuple2<Date,Boolean>> filted_result=integerTuple2JavaPairRDD.filter(new Function<Tuple2<Integer, Tuple2<Date, Boolean>>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Integer, Tuple2<Date, Boolean>> integerTuple2Tuple2) throws Exception {
                        return integerTuple2Tuple2._2._2;
                    }
                });
                JavaRDD<Integer> output_data = filted_result.map(new Function<Tuple2<Integer, Tuple2<Date, Boolean>>, Integer>() {
                    @Override
                    public Integer call(Tuple2<Integer, Tuple2<Date, Boolean>> integerTuple2Tuple2) throws Exception {
                        return integerTuple2Tuple2._1;
                    }
                });
                return output_data;
            }
        });
        //output.print();


        //输出数据(未测试)
        output.foreachRDD((rdd,time)->{
            String output_url="/result/out.log";
            HdfsOperation.openHdfsFile(output_url);
        rdd.foreach((rdddata)->{

            HdfsOperation.writeString(rdddata.toString());
        });
    });
        jsc.start();              // 开始
        jsc.awaitTermination();   // 等待计算终止
        jsc.close();
    }
}