package com.example.ahmed.gp_posts;

public class Posts {
    public String uid, time, date, postimage, description, fullname;

    public Posts() {
    }

    public Posts(String uid, String time, String date, String postimage, String description, String fullname) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getDescription() {
        return description;
    }


    public String getFullname() {
        return fullname;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public void setDescription(String description) {
        this.description = description;}

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
