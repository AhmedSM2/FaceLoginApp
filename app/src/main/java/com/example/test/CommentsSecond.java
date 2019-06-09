package com.example.test;

public class CommentsSecond {

    public String comment;
    public String username;
    public String date;
    public String time;
    public String image_uri;
    public String profile_image;

    public CommentsSecond(String comment, String username, String date, String time, String image_uri, String profile_image) {
        this.comment = comment;
        this.username = username;
        this.date = date;
        this.time = time;
        this.image_uri = image_uri;
        this.profile_image = profile_image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public CommentsSecond(){

    }

}