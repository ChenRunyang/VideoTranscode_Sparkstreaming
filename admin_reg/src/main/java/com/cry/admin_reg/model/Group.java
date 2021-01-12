package com.cry.admin_reg.model;

public class Group {
    private Integer id;
    private String name;
    private Integer user;
    private String describe;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getUser() {
        return user;
    }

    public String getDescribe() {
        return describe;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
