package com.example.ahmed.gp_posts;

public class User {
    private String ImageUrl;
    private String  email;
    private String uid;
    private int user_type;
    private String username;
    private String status;


    public User(String imageUrl, String email, String uid, int user_type, String username,String status) {
        ImageUrl = imageUrl;
        this.email = email;
        this.uid = uid;
        this.user_type = user_type;
        this.username = username;
        this.status = status;
    }

    public User() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
