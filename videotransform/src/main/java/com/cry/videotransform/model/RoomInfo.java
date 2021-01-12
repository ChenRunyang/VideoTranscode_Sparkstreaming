package com.cry.videotransform.model;

public class RoomInfo {
    int roomid;
    String password;
    String m3u8_path;
    String state;

    public int getRoomid() {
        return roomid;
    }

    public String getPassword() {
        return password;
    }

    public String getM3u8_path() {
        return m3u8_path;
    }

    public String getState() {
        return state;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setM3u8_path(String m3u8_path) {
        this.m3u8_path = m3u8_path;
    }

    public void setState(String state) {
        this.state = state;
    }
}
