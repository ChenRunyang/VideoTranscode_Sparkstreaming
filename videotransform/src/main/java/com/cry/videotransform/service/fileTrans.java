package com.cry.videotransform.service;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVOutputFormat;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import static org.bytedeco.ffmpeg.global.avcodec.*;
import static org.bytedeco.ffmpeg.global.avformat.*;

@Service
public class fileTrans {

    public static ArrayList<String> divideFile(String filepath,String format, int seconds) {
        ArrayList<String> dividedfile=new ArrayList<>();
        AVFormatContext ifmtCtx = new AVFormatContext(null);
        AVFormatContext ofmtCtx = new AVFormatContext(null);
        AVPacket readPkt = null;
        AVPacket splitKeyPacket = null;
        int ret;
        int video_index = 0;
        av_register_all();
        if (avformat_open_input(ifmtCtx, filepath, null, null) != 0) {
            return dividedfile;
        }
        for (int i = 0; i < ifmtCtx.nb_streams(); i++) {
            AVStream in_stream = ifmtCtx.streams(i);
            video_index = i;
        }
        int den = ifmtCtx.streams(video_index).r_frame_rate().den();
        int num = ifmtCtx.streams(video_index).r_frame_rate().num();
        float fps = (float) num / den;
        int splitVideoSize = (int) fps * seconds;
        String out_name = filepath.substring(0, filepath.lastIndexOf("."));
        String tmp_name = out_name+ format + "0.mp4";
        avformat_alloc_output_context2(ofmtCtx, null, null, tmp_name);
        if (ofmtCtx == null) {
            return dividedfile;
        }
        //写头文件
        AVOutputFormat ofmt = ofmtCtx.oformat();
        if ((ofmt.flags() & AVFMT_NOFILE) != 0) {
            ret = avio_open(ofmtCtx.pb(), tmp_name, AVIO_FLAG_WRITE);
            if (ret < 0) {
                return dividedfile;
            }
            ret = avformat_write_header(ofmtCtx, (AVDictionary) null);
            if (ret < 0) {
                return dividedfile;
            }
        }
        Vector<Integer> vecKeyFramePos = new Vector<>();
        Integer frame_index = 0;
        int keyFrame_index = 0;
        int framecount = 0;
        while (true) {
            ++frame_index;
            ret = av_read_frame(ifmtCtx, readPkt);
            if (ret < 0) {
                break;
            }
            //处理视频流
            if (readPkt.stream_index() == video_index) {
                ++framecount;
                if ((readPkt.flags() & 0x0001) != 0)  //是关键帧
                {
                    keyFrame_index = frame_index;
                }
                if (framecount > splitVideoSize)             //已经切割完毕
                {
                    vecKeyFramePos.add(keyFrame_index);
                    framecount = 0;
                }
            }
            readPkt.close();
        }
        avformat_close_input(ifmtCtx);
        ifmtCtx = null;
        if (avformat_open_input(ifmtCtx, filepath, null, null) != 0) {
            return dividedfile;
        }
        if ((ret = avformat_find_stream_info(ifmtCtx, 0)) < 0) {
            return dividedfile;
        }
        int number = 0;
        av_init_packet(splitKeyPacket);
        splitKeyPacket.data(null);
        splitKeyPacket.size(0);
        if (vecKeyFramePos.isEmpty()) {
            vecKeyFramePos.add(frame_index);
        }
        int keyframe_index = 0;
        int keyframe_iter = 0;
        long lastPts = 0;
        long lastDts = 0;
        long prePts = 0;
        long preDts = 0;
        ++keyframe_iter;
        while (true) {
            ++frame_index;
            ret = av_read_frame(ifmtCtx, readPkt);
            if (ret < 0) {
                break;
            }
            av_packet_rescale_ts(readPkt, ifmtCtx.streams(readPkt.stream_index()).time_base(), ofmtCtx.streams(readPkt.stream_index()).time_base());
            prePts = readPkt.pts();
            preDts = readPkt.dts();
            readPkt.pts(readPkt.pts() - lastPts);
            readPkt.dts(readPkt.dts() - lastDts);
            if (readPkt.pts() < readPkt.dts()) {
                readPkt.pts(readPkt.dts() + 1);
            }
            if (((readPkt.flags() & 0x0001) != 0) && frame_index == keyframe_index) {
                av_copy_packet(splitKeyPacket, readPkt);
            } else {
                ret = av_interleaved_write_frame(ofmtCtx, readPkt);
                if (ret < 0) {
                    //break;
                }
            }
            if (frame_index == keyFrame_index) {
                lastDts = preDts;
                lastPts = prePts;
                int flag = 0;
                if (vecKeyFramePos.get(flag) != vecKeyFramePos.get(vecKeyFramePos.size())) {
                    keyFrame_index = vecKeyFramePos.get(flag);
                    ++flag;
                }
                av_write_trailer(ofmtCtx);
                avio_close(ofmtCtx.pb());
                avformat_free_context(ofmtCtx);
                ++number;
                tmp_name = out_name + format + String.valueOf(number) + ".mp4";
                avformat_alloc_output_context2(ofmtCtx, null, null, tmp_name);
                if (ofmtCtx == null) {
                    return dividedfile;
                }
                ofmt = ofmtCtx.oformat();
                if ((ofmt.flags() & AVFMT_NOFILE) != 0) {
                    ret = avio_open(ofmtCtx.pb(), tmp_name, AVIO_FLAG_WRITE);
                    if (ret < 0) {
                        return dividedfile;
                    }
                    ret = avformat_write_header(ofmtCtx, (AVDictionary) null);
                    if (ret < 0) {
                        return dividedfile;
                    }
                }
                splitKeyPacket.pts(0);
                splitKeyPacket.dts(0);
                ret = av_interleaved_write_frame(ofmtCtx, splitKeyPacket);
            }
            dividedfile.add(tmp_name);       //把已转码的视频路径添加到列表
            readPkt.close();
        }
        splitKeyPacket.close();
        av_write_trailer(ofmtCtx);
        avformat_close_input(ifmtCtx);
        avio_close(ofmtCtx.pb());
        avformat_free_context(ofmtCtx);
        return dividedfile;
    }

}
