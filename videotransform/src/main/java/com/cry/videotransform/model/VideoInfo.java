package com.cry.videotransform.model;

public class VideoInfo {
    private int id;
    private String name;
    private String videopath;
    private String sendtime;
    private int videotime;
    private int groupid;
    private int userid;
    private int width;
    private int height;

    public void setVideotime(int videotime) {
        this.videotime = videotime;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVideopath() {
        return videopath;
    }

    public String getSendtime() {
        return sendtime;
    }

    public int getUserid() {
        return userid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGroupid() {
        return groupid;
    }

    public int getVideotime() {
        return videotime;
    }
}
