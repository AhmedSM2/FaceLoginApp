package com.example.test;

public class PostSecond {
    public String date,desc,image_uri,time,profile_image,username,uid;

    public PostSecond() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PostSecond(String date, String desc, String image_uri, String time, String profile_image, String username) {
        this.date = date;
        this.desc = desc;
        this.image_uri = image_uri;
        this.time = time;
        this.profile_image = profile_image;
        this.username = username;
    }


}
