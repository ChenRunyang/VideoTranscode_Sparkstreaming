package com.cry.admin_reg.model;

public class User {
    private Integer id;
    private String name;
    private String password;
    private String sex;
    private String email;
    private String phone;
    private String picture;
    private String video;
    private String roomname;
    private String roompwd;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public void setRoompwd(String roompwd) {
        this.roompwd = roompwd;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPicture() {
        return picture;
    }

    public String getVideo() {
        return video;
    }

    public String getRoomname() {
        return roomname;
    }

    public String getRoompwd() {
        return roompwd;
    }
}
